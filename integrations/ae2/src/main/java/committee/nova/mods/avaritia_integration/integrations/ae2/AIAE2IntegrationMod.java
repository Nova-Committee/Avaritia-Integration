package committee.nova.mods.avaritia_integration.integrations.ae2;

import committee.nova.mods.avaritia_integration.api.load.IntegrationRuntime;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(AIAE2IntegrationMod.MOD_ID)
public final class AIAE2IntegrationMod {

    public static final String MOD_ID = "avaritia_integration_ae2";

    public AIAE2IntegrationMod(IEventBus bus, ModContainer modContainer) {
        if (IntegrationRuntime.load(MOD_ID, bus, AE2Module::new)) ModRun.init(bus);
    }
}
