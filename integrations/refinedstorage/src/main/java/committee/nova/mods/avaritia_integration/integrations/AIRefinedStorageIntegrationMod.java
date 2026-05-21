package committee.nova.mods.avaritia_integration.integrations;

import committee.nova.mods.avaritia_integration.api.load.IntegrationRuntime;
import committee.nova.mods.avaritia_integration.integrations.refinedstorage.RefinedStorageModule;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(AIRefinedStorageIntegrationMod.MOD_ID)
public final class AIRefinedStorageIntegrationMod {

    public static final String MOD_ID = "avaritia_integration_refinedstorage";

    public AIRefinedStorageIntegrationMod(IEventBus bus, ModContainer modContainer) {
        IntegrationRuntime.load(MOD_ID, bus, RefinedStorageModule::new);
    }
}
