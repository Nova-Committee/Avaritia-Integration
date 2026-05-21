package committee.nova.mods.avaritia_integration.integrations;

import committee.nova.mods.avaritia_integration.api.load.IntegrationRuntime;
import committee.nova.mods.avaritia_integration.integrations.pneumaticcraft.PneumaticCraftModule;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(AIPneumaticCraftIntegrationMod.MOD_ID)
public final class AIPneumaticCraftIntegrationMod {

    public static final String MOD_ID = "avaritia_integration_pneumaticcraft";

    public AIPneumaticCraftIntegrationMod(IEventBus bus, ModContainer modContainer) {
        if (IntegrationRuntime.load(MOD_ID, bus, PneumaticCraftModule::new)) ModRun.init(bus);
    }
}
