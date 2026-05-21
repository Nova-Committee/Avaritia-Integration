package committee.nova.mods.avaritia_integration.api.load;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.module.Module;

import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class IntegrationRuntime {

    private static final Map<String, Module> LOADED_MODULES = new LinkedHashMap<>();

    private IntegrationRuntime() {}

    public static boolean load(String integrationModId, IEventBus modBus, Supplier<? extends Module> moduleFactory) {
        LoadDecision decision = IntegrationLoadApi.explain(integrationModId);
        if (!decision.shouldLoad()) {
            AvaritiaIntegration.LOGGER.info("Skip integration {}: {}", integrationModId, decision.message());
            return false;
        }
        Module module = moduleFactory.get();
        try {
            module.init(modBus);
            module.registerEvent(modBus, NeoForge.EVENT_BUS);
            if (FMLEnvironment.dist.isClient()) {
                module.initClient();
                module.registerClientEvent(modBus, NeoForge.EVENT_BUS);
            }
            modBus.addListener((FMLCommonSetupEvent event) -> process(integrationModId, module));
            LOADED_MODULES.put(integrationModId, module);
            AvaritiaIntegration.LOGGER.info("Loaded integration {}", integrationModId);
            return true;
        } catch (Exception e) {
            AvaritiaIntegration.LOGGER.error("Failed to load integration {}.", integrationModId, e);
            return false;
        }
    }

    public static void collectCreativeTabItems(CreativeModeTab.ItemDisplayParameters parameters,
                                               CreativeModeTab.Output output) {
        LOADED_MODULES.forEach((id, module) -> {
            try {
                module.collectCreativeTabItems(parameters, output);
            } catch (Exception e) {
                AvaritiaIntegration.LOGGER.error("Failed to append creative tab items for integration {}.", id, e);
            }
        });
    }

    private static void process(String integrationModId, Module module) {
        try {
            module.process();
            if (FMLEnvironment.dist.isClient()) module.processClient();
        } catch (Exception e) {
            AvaritiaIntegration.LOGGER.error("Failed to process integration {}.", integrationModId, e);
        }
    }
}
