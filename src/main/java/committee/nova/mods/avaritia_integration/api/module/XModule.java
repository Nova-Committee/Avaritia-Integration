package committee.nova.mods.avaritia_integration.api.module;

import com.electronwill.nightconfig.core.utils.StringUtils;
import com.google.common.base.Strings;
import com.google.common.collect.*;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.mojang.logging.LogUtils;
import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.api.LoadingContext;
import committee.nova.mods.avaritia_integration.api.ModContext;
import committee.nova.mods.avaritia_integration.api.module.InModule.Subscriber;
import committee.nova.mods.avaritia_integration.api.config.AnnotatedTypeLoader;
import committee.nova.mods.avaritia_integration.api.config.XConfiguration;
import committee.nova.mods.avaritia_integration.api.event.ClientInitEvent;
import committee.nova.mods.avaritia_integration.api.event.InitEvent;
import committee.nova.mods.avaritia_integration.api.event.PostInitEvent;
import committee.nova.mods.avaritia_integration.api.event.ServerInitEvent;
import committee.nova.mods.avaritia_integration.api.util.Util;
import committee.nova.mods.avaritia_integration.api.util.schedule.Scheduler;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.*;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.toposort.TopologicalSort;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: cnlimiter
 */
public class XModule {
    public static final String ID = AvaritiaIntegration.MOD_ID;

    public static final Logger LOGGER = LogUtils.getLogger();
    static final Marker MARKER = MarkerFactory.getMarker("INIT");

    public static Map<ResourceLocation, Boolean> defaultOptions = Maps.newHashMap();
    private static Multimap<String, XAnnotationData> moduleData = ArrayListMultimap.create();
    private static Map<XAnnotationData, String> conditions = Maps.newHashMap();
    private static LoadingStage stage = LoadingStage.UNINITED;

    // 添加用于跟踪模块依赖关系的数据结构
    private static Map<ResourceLocation, Set<String>> moduleDependencies = Maps.newHashMap();

    public static void init(IEventBus modEventBus) throws Exception {
        if (stage != LoadingStage.UNINITED) {
            return;
        }
        stage = LoadingStage.CONSTRUCTING;
        Map<String, XAnnotationData> classOptionalMap = Maps.newHashMap();
        String dist = FMLEnvironment.dist.isClient() ? "client" : "server";
        List<String> mods = ModList.get().getMods().stream().map(IModInfo::getModId).toList();
        for (String mod : mods) {
            if (ResourceLocation.DEFAULT_NAMESPACE.equals(mod) || "forge".equals(mod)) {
                continue;
            }
            AnnotatedTypeLoader loader = new AnnotatedTypeLoader(mod);
            XConfiguration configuration = loader.get();
            if (configuration == null) {
                continue;
            }

            for (XAnnotationData module : configuration.modules) {
                if (!checkDist(module, dist)) {
                    continue;
                }
                moduleData.put(mod, module);
            }
        }

        LOGGER.info(MARKER, "Processing " + moduleData.size() + " KiwiModule annotations");

        for (Map.Entry<String, XAnnotationData> entry : moduleData.entries()) {
            XAnnotationData optional = classOptionalMap.get(entry.getValue().target());
            if (optional != null) {
                String modid = entry.getKey();
                if (!ModList.get().isLoaded(modid)) {
                    continue;
                }

                String name = (String) entry.getValue().data().get("value");
                if (Strings.isNullOrEmpty(name)) {
                    name = "core";
                }

                Boolean defaultEnabled = (Boolean) optional.data().get("defaultEnabled");
                if (defaultEnabled == null) {
                    defaultEnabled = Boolean.TRUE;
                }
                defaultOptions.put(new ResourceLocation(modid, name), defaultEnabled);
            }
        }

        //XConfigManager.init();
        modEventBus.addListener(XModule::commonInit);
        modEventBus.addListener(XModule::clientInit);
        MinecraftForge.EVENT_BUS.addListener(XModule::serverInit);
        modEventBus.addListener(XModule::postInit);

        stage = LoadingStage.CONSTRUCTED;
    }

    private static boolean checkDist(XAnnotationData annotationData, String dist) throws IOException {
        try {
            ClassNode clazz = new ClassNode(Opcodes.ASM7);
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(
                    annotationData.target().replace('.', '/') + ".class");
            final ClassReader classReader = new ClassReader(is);
            classReader.accept(clazz, 0);
            if (clazz.visibleAnnotations != null) {
                final String ONLYIN = Type.getDescriptor(OnlyIn.class);
                for (AnnotationNode node : clazz.visibleAnnotations) {
                    if (node.values != null && ONLYIN.equals(node.desc)) {
                        int i = node.values.indexOf("value");
                        if (i != -1 && !node.values.get(i + 1).equals(dist)) {
                            return false;
                        }
                    }
                }
            }
        } catch (Throwable e) {
            return false;
        }
        return true;
    }

    public static void preInit() {
        if (stage != LoadingStage.CONSTRUCTED) {
            return;
        }
        Set<ResourceLocation> disabledModules = Sets.newHashSet();
        conditions.forEach((k, v) -> {
            try {
                Class<?> clazz = Class.forName(k.target());
                String methodName = (String) k.data().get("method");
                List<String> values = (List<String>) k.data().get("value");
                if (values == null) {
                    values = List.of(v);
                }
                List<ResourceLocation> ids = values.stream().map(s -> checkPrefix(s, v)).collect(Collectors.toList());
                for (ResourceLocation id : ids) {
                    LoadingContext context = new LoadingContext(id);
                    try {
                        Boolean bl = (Boolean) MethodUtils.invokeExactStaticMethod(clazz, methodName, context);
                        if (!bl) {
                            disabledModules.add(id);
                        }
                    } catch (Exception e) {
                        disabledModules.add(id);
                        throw e;
                    }
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
                LOGGER.error(MARKER, "Failed to access to LoadingCondition: %s".formatted(k), e);
            }
        });

        final Map<ResourceLocation, Info> infos = Maps.newHashMap();
        boolean checkDep = false;

        load:
        for (Map.Entry<String, XAnnotationData> entry : moduleData.entries()) {
            XAnnotationData module = entry.getValue();
            String modid = entry.getKey();
            if (!ModList.get().isLoaded(modid)) {
                continue;
            }

            String name = (String) module.data().get("value");
            if (Strings.isNullOrEmpty(name)) {
                name = "core";
            }

            ResourceLocation rl = new ResourceLocation(modid, name);
            if (disabledModules.contains(rl)) {
                continue;
            }

            Info info = new Info(rl, module.target());

            String dependencies = (String) module.data().get("dependencies");
            /* off */
            List<String> rules = StringUtils.split(Strings.nullToEmpty(dependencies), ';')
                    .stream()
                    .filter(s -> !Strings.isNullOrEmpty(s))
                    .toList();
            /* on */

            // 保存模块依赖信息
            Set<String> modDependencies = rules.stream()
                    .filter(rule -> !rule.startsWith("@")) // 过滤掉模块间依赖
                    .collect(Collectors.toSet());
            moduleDependencies.put(rl, modDependencies);

            for (String rule : rules) {
                if (rule.startsWith("@")) {
                    info.moduleRules.add(Util.RL(rule.substring(1), modid));
                    checkDep = true;
                } else if (!ModList.get().isLoaded(rule)) {
                    continue load;
                }
            }
            infos.put(rl, info);
        }
        List<ResourceLocation> moduleLoadingQueue = null;
        if (checkDep) {
            List<Info> errorList = Lists.newLinkedList();
            for (Info i : infos.values()) {
                for (ResourceLocation id : i.moduleRules) {
                    if (!infos.containsKey(id)) {
                        errorList.add(i);
                        break;
                    }
                }
            }
            for (Info i : errorList) {
                IModInfo modInfo = ModList.get().getModContainerById(i.id.getNamespace()).get().getModInfo();
                String dependencies = org.apache.commons.lang3.StringUtils.join(i.moduleRules, ", ");
                ModLoader.get().addWarning(new ModLoadingWarning(
                        modInfo,
                        ModLoadingStage.ERROR,
                        "msg.kiwi.no_dependencies",
                        i.id,
                        dependencies));
            }
            if (!errorList.isEmpty()) {
                return;
            }
            MutableGraph<ResourceLocation> graph = GraphBuilder.directed().allowsSelfLoops(false).expectedNodeCount(infos.size()).build();
            infos.keySet().forEach(graph::addNode);
            infos.values().forEach($ -> {
                $.moduleRules.forEach(r -> graph.putEdge(r, $.id));
            });
            moduleLoadingQueue = TopologicalSort.topologicalSort(graph, null);
        } else {
            moduleLoadingQueue = ImmutableList.copyOf(infos.keySet());
        }

        for (ResourceLocation id : moduleLoadingQueue) {
            Info info = infos.get(id);
            ModContext context = ModContext.get(id.getNamespace());
            context.setActiveContainer();

            // Instantiate modules
            try {
                Class<?> clazz = Class.forName(info.className);
                AbModule instance = (AbModule) clazz.getDeclaredConstructor().newInstance();
                XModules.add(id, instance, context);
            } catch (Exception e) {
                LOGGER.error(MARKER, "Kiwi failed to initialize module class: %s".formatted(info.className), e);
                continue;
            }

            ModLoadingContext.get().setActiveContainer(null);
        }

        moduleData.clear();
        moduleData = null;
        defaultOptions.clear();
        defaultOptions = null;
        conditions.clear();
        conditions = null;

        Object2IntMap<ResourceKey<?>> counter = new Object2IntArrayMap<>();
        for (ModuleInfo info : XModules.get()) {
            counter.clear();
            info.context.setActiveContainer();
            Subscriber subscriber = info.module.getClass().getDeclaredAnnotation(Subscriber.class);
            if (subscriber != null && (!subscriber.clientOnly() || FMLEnvironment.dist.isClient())) {
                // processEvents(info.module);
                IEventBus eventBus;
                if (subscriber.modBus()) {
                    eventBus = FMLJavaModLoadingContext.get().getModEventBus();
                } else {
                    eventBus = MinecraftForge.EVENT_BUS;
                }
                eventBus.register(info.module);
            }


            String modid = info.module.uid.getNamespace();
            String name = info.module.uid.getPath();

            if (ID.equals(modid) && name.startsWith("contributors")) {
                continue;
            }
            LOGGER.info(MARKER, "Module [{}:{}] initialized", modid, name);
            List<String> entries = Lists.newArrayList();
            for (ResourceKey<?> key : counter.keySet()) {
                entries.add("%s: %s".formatted(Util.trimRL(key.location()), counter.getInt(key)));
            }
            if (!entries.isEmpty()) {
                LOGGER.info(MARKER, "\t\t" + String.join(", ", entries));
            }
        }

        XModules.fire(ModuleInfo::register, XModule::shouldRegisterModule);
        XModules.fire(ModuleInfo::preInit);
        ModLoadingContext.get().setActiveContainer(null);
        stage = LoadingStage.INITED;
    }

    private static void commonInit(FMLCommonSetupEvent event) {
        InitEvent e = new InitEvent(event);
        XModules.fire(m -> m.init(e));
        ModLoadingContext.get().setActiveContainer(null);
    }

    private static void clientInit(FMLClientSetupEvent event) {
        ClientInitEvent e = new ClientInitEvent(event);
        XModules.fire(m -> m.clientInit(e));
        ModLoadingContext.get().setActiveContainer(null);
    }

    private static void serverInit(ServerStartingEvent event) {
        ServerInitEvent e = new ServerInitEvent();
        XModules.fire(m -> m.serverInit(e));
        event.getServer().getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(
                Scheduler::load,
                () -> Scheduler.INSTANCE,
                Scheduler.ID);
        ModLoadingContext.get().setActiveContainer(null);
    }

    private static void postInit(InterModProcessEvent event) {
        PostInitEvent e = new PostInitEvent(event);
        XModules.fire(m -> m.postInit(e));
        ModLoadingContext.get().setActiveContainer(null);
        XModules.clear();
    }

    public static boolean isLoaded(ResourceLocation module) {
        return XModules.isLoaded(module);
    }


    private static ResourceLocation checkPrefix(String name, String defaultModid) {
        if (name.contains(":")) {
            return new ResourceLocation(name);
        } else {
            return new ResourceLocation(defaultModid, name);
        }
    }

    // 添加依赖检查方法
    private static boolean shouldRegisterModule(ModuleInfo moduleInfo) {
        ResourceLocation moduleId = moduleInfo.module.uid;
        Set<String> dependencies = moduleDependencies.get(moduleId);

        if (dependencies == null || dependencies.isEmpty()) {
            return true; // 没有依赖，总是注册
        }

        // 检查所有依赖是否都已加载
        return dependencies.stream().allMatch(ModList.get()::isLoaded);
    }

    private enum LoadingStage {
        UNINITED, CONSTRUCTING, CONSTRUCTED, INITED;
    }

    private static final class Info {
        final ResourceLocation id;
        final String className;
        final List<ResourceLocation> moduleRules = Lists.newLinkedList();

        public Info(ResourceLocation id, String className) {
            this.id = id;
            this.className = className;
        }
    }
}
