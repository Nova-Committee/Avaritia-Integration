package committee.nova.mods.avaritia_integration.integrations.industrialforegoing;

import committee.nova.mods.avaritia_integration.api.load.IntegrationRuntime;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(AIIndustrialForegoingIntegrationMod.MOD_ID)
public final class AIIndustrialForegoingIntegrationMod {

    public static final String MOD_ID = "avaritia_integration_industrialforegoing";

    public AIIndustrialForegoingIntegrationMod(IEventBus bus, ModContainer modContainer) {
        IntegrationRuntime.load(MOD_ID, bus, IndustrialForegoingModule::new);
    }
}
