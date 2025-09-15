package committee.nova.mods.avaritia_integration.module;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.moddiscovery.ModAnnotation;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Type;
import org.slf4j.Logger;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * Find and load all modules
 *
 * @author IAFEnvoy
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModuleManager {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final List<ModuleData> ALL_MODULES = ModList.get()
            .getAllScanData()
            .stream().flatMap(x -> x.getAnnotations().stream())
            .filter(x -> x.annotationType().equals(Type.getType(ModuleEntry.class)))
            .map(ModuleData::parse)
            .toList();
    private static final Map<ModuleData, Module> ENABLED_MODULES = ALL_MODULES
            .stream()
            .filter(ModuleData::enabled)
            .collect(LinkedHashMap::new, (p, c) -> p.put(c, createModule(c)), HashMap::putAll);

    @Nullable
    private static Module createModule(ModuleData data) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(data.className);
        } catch (Exception e) {
            ModuleManager.LOGGER.error("Failed to get class", e);
        }
        if (clazz == null) return null;
        Object obj = null;
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            obj = constructor.newInstance();
        } catch (Exception var2) {
            ModuleManager.LOGGER.error("Failed to construct object");
        }
        return obj instanceof Module module ? module : null;
    }

    public static EnableState getState(String value, String minVersion, String maxVersion) {
        Optional<? extends ModContainer> container = ModList.get().getModContainerById(value);
        if (container.isEmpty()) return EnableState.MISSING_MOD;
        IModInfo info = container.get().getModInfo();
        if (!minVersion.isEmpty()) {
            ArtifactVersion min = new DefaultArtifactVersion(minVersion);
            if (info.getVersion().compareTo(min) < 0) return EnableState.VERSION_TOO_LOW;
        }
        if (!maxVersion.isEmpty()) {
            ArtifactVersion max = new DefaultArtifactVersion(maxVersion);
            if (info.getVersion().compareTo(max) > 0) return EnableState.VERSION_TOO_HIGH;
        }
        //TODO::Manual disable module
        return EnableState.ENABLED;
    }

    @ApiStatus.Internal
    public static void loadModules(IEventBus modBus) {
        List<String> scanned = ALL_MODULES.stream().map(ModuleData::id).toList();
        ModuleManager.LOGGER.info("Scanned {} modules: {}", scanned.size(), String.join(", ", scanned));
        List<String> initializing = ENABLED_MODULES.keySet().stream().map(ModuleData::id).toList();
        ModuleManager.LOGGER.info("Start initializing {} modules: {}", initializing.size(), String.join(", ", initializing));
        List<String> initialized = new LinkedList<>();
        for (Map.Entry<ModuleData, Module> entry : ENABLED_MODULES.entrySet()) {
            ModuleData data = entry.getKey();
            try {
                Module module = entry.getValue();
                module.init();
                module.registerEvent(modBus, MinecraftForge.EVENT_BUS);
                initialized.add(data.id);
            } catch (Exception e) {
                ModuleManager.LOGGER.error("Failed to setup module {}.", data.id, e);
            }
        }
        ModuleManager.LOGGER.info("Successfully initialized {} modules: {}", initialized.size(), String.join(", ", initialized));
    }

    @ApiStatus.Internal
    @SubscribeEvent
    public static void postProcess(FMLCommonSetupEvent event) {
        List<String> processing = ENABLED_MODULES.keySet().stream().map(ModuleData::id).toList();
        ModuleManager.LOGGER.info("Start processing {} modules: {}", processing.size(), String.join(", ", processing));
        List<String> processed = new LinkedList<>();
        for (Map.Entry<ModuleData, Module> entry : ENABLED_MODULES.entrySet()) {
            ModuleData data = entry.getKey();
            try {
                Module module = entry.getValue();
                module.process();
                processed.add(data.id);
            } catch (Exception e) {
                ModuleManager.LOGGER.error("Failed to setup module {}.", data.id, e);
            }
        }
        ModuleManager.LOGGER.info("Successfully processed {} modules: {}", processed.size(), String.join(", ", processed));
    }

    public record ModuleData(String id, String className, List<EnableState> states, List<Dist> dist) {
        @SuppressWarnings("unchecked")
        public static ModuleData parse(ModFileScanData.AnnotationData data) {
            String className = data.memberName();
            Map<String, Object> annotationData = data.annotationData();
            String id = annotationData.get("id").toString();
            List<Map<String, Object>> target = (List<Map<String, Object>>) annotationData.getOrDefault("target", List.of());
            List<ModAnnotation.EnumHolder> sideHolder = (List<ModAnnotation.EnumHolder>) annotationData.get("side");
            List<Dist> side;
            if (sideHolder == null) side = List.of(Dist.CLIENT, Dist.DEDICATED_SERVER);
            else
                side = sideHolder.stream().map(x -> "CLIENT".equals(x.getValue()) ? Dist.CLIENT : Dist.DEDICATED_SERVER).toList();
            return new ModuleData(id, className, target.stream().map(m -> getState(m.get("value").toString(), m.getOrDefault("minVersion", "").toString(), m.getOrDefault("maxVersion", "").toString())).toList(), side);
        }

        public boolean enabled() {
            return this.dist.contains(FMLEnvironment.dist) && this.states.stream().filter(x -> x != EnableState.ENABLED).findAny().isEmpty();
        }
    }

    public enum EnableState {
        ENABLED, DISABLED, MISSING_MOD, VERSION_TOO_HIGH, VERSION_TOO_LOW
    }
}
