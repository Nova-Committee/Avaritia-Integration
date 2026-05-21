package committee.nova.mods.avaritia_integration.module;

import com.mojang.logging.LogUtils;
import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.util.ConfigLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforgespi.language.IModInfo;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Type;
import org.slf4j.Logger;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Find and load all modules
 *
 * @author IAFEnvoy
 */
@EventBusSubscriber( modid = AvaritiaIntegration.MOD_ID)
public final class ModuleManager {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String DISABLE_CONFIG_PATH = "./config/avaritia/integration/disabled_modules.json";
    private static final DisableConfig DISABLED_MODULES = ConfigLoader.load(DisableConfig.class, DISABLE_CONFIG_PATH, new DisableConfig());
    private static final List<ModuleData> ALL_MODULES = ModList.get()
            .getAllScanData()
            .stream().flatMap(x -> x.getAnnotations().stream())
            .filter(x -> x.annotationType().equals(Type.getType(ModuleEntry.class)))
            .map(ModuleData::parse)
            .toList();
    private static final Map<ModuleData, Module> ENABLED_MODULES = ALL_MODULES
            .stream()
            .filter(x -> x.getEnableState().shouldLoad())
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

    public static List<ModuleData> getAllModules() {
        return ALL_MODULES;
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
                if (module == null) continue;
                module.init(modBus);
                module.registerEvent(modBus, NeoForge.EVENT_BUS);
                if (FMLEnvironment.dist.isClient()) {
                    module.initClient();
                    module.registerClientEvent(modBus, NeoForge.EVENT_BUS);
                }
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
                if (module == null) continue;
                module.process();
                if (FMLEnvironment.dist.isClient()) module.processClient();
                processed.add(data.id);
            } catch (Exception e) {
                ModuleManager.LOGGER.error("Failed to post module {}.", data.id, e);
            }
        }
        ModuleManager.LOGGER.info("Successfully processed {} modules: {}", processed.size(), String.join(", ", processed));
    }

    @ApiStatus.Internal
    public static void collectCreativeTabItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        for (Map.Entry<ModuleData, Module> entry : ENABLED_MODULES.entrySet()) {
            ModuleData data = entry.getKey();
            try {
                Module module = entry.getValue();
                if (module == null) continue;
                module.collectCreativeTabItems(parameters, output);
            } catch (Exception e) {
                ModuleManager.LOGGER.error("Failed to append creative tab items {}.", data.id, e);
            }
        }
    }

    private static Optional<ArtifactVersion> getModVersion(String id) {
        return ModList.get().getModContainerById(id).map(ModContainer::getModInfo).map(IModInfo::getVersion);
    }

    private static ErrorType getState(String id, String minVersion, String maxVersion) {
        Optional<ArtifactVersion> optional = getModVersion(id);
        if (optional.isEmpty()) return ErrorType.MISSING_MOD;
        ArtifactVersion version = optional.get();
        if (!minVersion.isEmpty()) {
            ArtifactVersion min = new DefaultArtifactVersion(minVersion);
            if (version.compareTo(min) < 0) return ErrorType.VERSION_TOO_LOW;
        }
        if (!maxVersion.isEmpty()) {
            ArtifactVersion max = new DefaultArtifactVersion(maxVersion);
            if (version.compareTo(max) > 0) return ErrorType.VERSION_TOO_HIGH;
        }
        return ErrorType.NONE;
    }

    public static DependencyData getDependencyData(String id, String minVersion, String maxVersion) {
        return new DependencyData(id, minVersion, maxVersion, getState(id, minVersion, maxVersion));
    }

    public static void switchEnableState(ModuleData data) {
        String id = data.id;
        if (DISABLED_MODULES.disabled().contains(id)) DISABLED_MODULES.disabled().remove(id);
        else DISABLED_MODULES.disabled().add(id);
        DISABLED_MODULES.save();
    }

    public record ModuleData(String id, String className, List<DependencyData> dependencies) {
        @SuppressWarnings("unchecked")
        public static ModuleData parse(ModFileScanData.AnnotationData data) {
            String className = data.memberName();
            Map<String, Object> annotationData = data.annotationData();
            String id = annotationData.get("id").toString();
            List<Map<String, Object>> target = (List<Map<String, Object>>) annotationData.getOrDefault("target", List.of());
            return new ModuleData(id, className, target.stream().map(m -> getDependencyData(m.get("value").toString(), m.getOrDefault("minVersion", "").toString(), m.getOrDefault("maxVersion", "").toString())).toList());
        }

        public EnableState getEnableState() {
            if (DISABLED_MODULES.disabled().contains(this.id)) return EnableState.DISABLED;
            return this.dependencies.stream().filter(x -> x.state != ErrorType.NONE).findAny().isEmpty() ? EnableState.ENABLED : EnableState.ERROR;
        }

        public MutableComponent getTranslateKey() {
            return Component.translatable("module.%s.name.%s".formatted(AvaritiaIntegration.MOD_ID, this.id));
        }

        public Component getStateKey() {
            EnableState state = this.getEnableState();
            return Component.translatable("screen.%s.state.%s".formatted(AvaritiaIntegration.MOD_ID, state.name().toLowerCase(Locale.ROOT))).withStyle(state.getColor());
        }

        public List<Component> getErrorTooltip() {
            EnableState state = this.getEnableState();
            if (!state.hasError()) return List.of();
            return this.dependencies.stream().map(DependencyData::getErrorMessage).filter(Optional::isPresent).map(Optional::get).toList();
        }
    }

    public record DependencyData(String id, String minVersion, String maxVersion, ErrorType state) {
        public Optional<Component> getErrorMessage() {
            String version = getModVersion(this.id).map(Object::toString).orElse("???");
            return switch (this.state) {
                case NONE -> Optional.empty();
                case MISSING_MOD ->
                        Optional.of(Component.translatable("screen.%s.message.missing_mod".formatted(AvaritiaIntegration.MOD_ID), this.formatVersionRange(), this.id));
                case VERSION_TOO_HIGH ->
                        Optional.of(Component.translatable("screen.%s.message.version_too_high".formatted(AvaritiaIntegration.MOD_ID), this.formatVersionRange(), this.id, version));
                case VERSION_TOO_LOW ->
                        Optional.of(Component.translatable("screen.%s.message.version_too_low".formatted(AvaritiaIntegration.MOD_ID), this.formatVersionRange(), this.id, version));
            };
        }

        public Component formatVersionRange() {
            if (this.minVersion.isEmpty()) {
                if (this.maxVersion.isEmpty())
                    return Component.translatable("screen.avaritia_integration.message.version_any");
                else
                    return Component.translatable("screen.avaritia_integration.message.version_lower", this.maxVersion);
            } else {
                if (this.maxVersion.isEmpty())
                    return Component.translatable("screen.avaritia_integration.message.version_higher", this.minVersion);
                else
                    return Component.translatable("screen.avaritia_integration.message.version_between", this.minVersion, this.maxVersion);
            }
        }
    }

    public record DisableConfig(List<String> disabled) {
        public DisableConfig() {
            this(new LinkedList<>());
        }

        public void save() {
            ConfigLoader.save(DISABLE_CONFIG_PATH, this);
        }
    }

    public enum EnableState {
        ENABLED(true, false, ChatFormatting.GREEN),
        DISABLED(false, false, ChatFormatting.YELLOW),
        ERROR(false, true, ChatFormatting.RED);
        private final boolean shouldLoad;
        private final boolean error;
        private final ChatFormatting color;

        EnableState(boolean shouldLoad, boolean error, ChatFormatting color) {
            this.shouldLoad = shouldLoad;
            this.error = error;
            this.color = color;
        }

        public boolean shouldLoad() {
            return this.shouldLoad;
        }

        public boolean hasError() {
            return this.error;
        }

        public ChatFormatting getColor() {
            return this.color;
        }
    }

    public enum ErrorType {
        NONE, MISSING_MOD, VERSION_TOO_HIGH, VERSION_TOO_LOW;
    }
}
