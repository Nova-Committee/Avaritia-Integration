package committee.nova.mods.avaritia_integration.module;

import com.iafenvoy.integration.util.ReflectUtil;
import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.jetbrains.annotations.ApiStatus;
import org.objectweb.asm.Type;

import java.util.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModuleManager {
    private static final List<ModuleData> ALL_MODULES = ModList.get()
            .getAllScanData()
            .stream().flatMap(x -> x.getAnnotations().stream())
            .filter(x -> x.annotationType().equals(Type.getType(ModuleEntry.class)))
            .map(ModuleData::parse)
            .toList();
    private static final List<Module> ENABLED_MODULES = ALL_MODULES
            .stream()
            .filter(ModuleData::enabled)
            .map(ModuleData::className)
            .map(ReflectUtil::getClassUnsafely)
            .filter(Objects::nonNull)
            .map(ReflectUtil::constructUnsafely)
            .filter(Objects::nonNull)
            .filter(x -> x instanceof Module)
            .map(Module.class::cast)
            .toList();

    public static EnableState getState(ModMeta meta) {
        Optional<? extends ModContainer> container = ModList.get().getModContainerById(meta.id());
        if (container.isEmpty()) return EnableState.MISSING_MOD;
        IModInfo info = container.get().getModInfo();
        if (!meta.minVersion().isEmpty()) {
            ArtifactVersion minVersion = new DefaultArtifactVersion(meta.minVersion());
            if (info.getVersion().compareTo(minVersion) < 0) return EnableState.VERSION_TOO_LOW;
        }
        if (!meta.maxVersion().isEmpty()) {
            ArtifactVersion maxVersion = new DefaultArtifactVersion(meta.maxVersion());
            if (info.getVersion().compareTo(maxVersion) > 0) return EnableState.VERSION_TOO_HIGH;
        }
        //TODO: Manual disable module
        return EnableState.ENABLED;
    }

    public static List<ModuleData> getAllModules() {
        return ALL_MODULES;
    }

    @ApiStatus.Internal
    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            for (Module module : ENABLED_MODULES) {
                try {
                    module.setup();
                    module.registerEvent(FMLJavaModLoadingContext.get().getModEventBus(), MinecraftForge.EVENT_BUS);
                } catch (Exception e) {
                    AvaritiaIntegration.LOGGER.error("Failed to setup module.", e);
                }
            }
        });
    }

    public record ModuleData(String id, String className, List<EnableState> states, List<Dist> dist) {
        public static ModuleData parse(ModFileScanData.AnnotationData data) {
            String className = data.memberName();
            Map<String, Object> annotationData = data.annotationData();
            String id = annotationData.get("id").toString();
            ModMeta[] targets = (ModMeta[]) annotationData.getOrDefault("targets", new ModMeta[0]);
            Dist[] side = (Dist[]) annotationData.getOrDefault("side", new Dist[]{Dist.CLIENT, Dist.DEDICATED_SERVER});
            return new ModuleData(id, className, Arrays.stream(targets).map(ModuleManager::getState).toList(), List.of(side));
        }

        public boolean enabled() {
            return this.dist.contains(FMLEnvironment.dist) && this.states.stream().filter(x -> x != EnableState.ENABLED).findAny().isEmpty();
        }
    }

    public enum EnableState {
        ENABLED, DISABLED, MISSING_MOD, VERSION_TOO_HIGH, VERSION_TOO_LOW
    }
}
