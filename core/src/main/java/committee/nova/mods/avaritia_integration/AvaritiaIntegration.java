package committee.nova.mods.avaritia_integration;

import committee.nova.mods.avaritia_integration.init.data.AIDataGen;
import committee.nova.mods.avaritia_integration.init.registry.AIBlocks;
import committee.nova.mods.avaritia_integration.init.registry.AICreativeTabs;
import committee.nova.mods.avaritia_integration.init.registry.AIFluidTypes;
import committee.nova.mods.avaritia_integration.init.registry.AIFluids;
import committee.nova.mods.avaritia_integration.init.registry.AIItems;
import committee.nova.mods.avaritia_integration.module.ModuleManager;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;

import net.neoforged.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

@Mod(AvaritiaIntegration.MOD_ID)
public class AvaritiaIntegration {

    public static final String MOD_ID = "avaritia_integration";
    public static final String MOD_NAME = "AvaritiaIntegration";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
    public static IEventBus MOD_EVENT_BUS;

    public AvaritiaIntegration(IEventBus bus, ModContainer modContainer) {
        MOD_EVENT_BUS = bus;

        AICreativeTabs.REGISTRY.register(bus);
        AIItems.REGISTRY.register(bus);
        AIBlocks.REGISTRY.register(bus);
        AIFluidTypes.REGISTRY.register(bus);
        AIFluids.REGISTRY.register(bus);
        ModuleManager.loadModules(bus);
        bus.addListener(AIDataGen::gatherData);
    }

    public static ResourceLocation rl(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    public static boolean isModLoaded(String name) {
        return ModList.get().isLoaded(name);
    }

    public static Path getGameDir() {
        return FMLPaths.CONFIGDIR.get();
    }
}
