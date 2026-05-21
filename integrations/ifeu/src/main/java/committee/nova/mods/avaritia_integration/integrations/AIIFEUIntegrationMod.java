package committee.nova.mods.avaritia_integration.integrations;

import committee.nova.mods.avaritia_integration.api.load.IntegrationRuntime;
import committee.nova.mods.avaritia_integration.integrations.ifeu.IFEUModule;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(AIIFEUIntegrationMod.MOD_ID)
public final class AIIFEUIntegrationMod {

    public static final String MOD_ID = "avaritia_integration_ifeu";

    public AIIFEUIntegrationMod(IEventBus bus, ModContainer modContainer) {
        IntegrationRuntime.load(MOD_ID, bus, IFEUModule::new);
    }
}
