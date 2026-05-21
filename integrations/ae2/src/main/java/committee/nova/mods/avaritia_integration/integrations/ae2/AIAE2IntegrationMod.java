package committee.nova.mods.avaritia_integration.integrations.ae2;

import committee.nova.mods.avaritia_integration.api.load.IntegrationDataPackRegistrar;
import committee.nova.mods.avaritia_integration.api.load.IntegrationRuntime;
import committee.nova.mods.avaritia_integration.integrations.ae2.datagen.AE2DataGen;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(AIAE2IntegrationMod.MOD_ID)
public final class AIAE2IntegrationMod {

    public static final String MOD_ID = "avaritia_integration_ae2";

    public AIAE2IntegrationMod(IEventBus bus, ModContainer modContainer) {
        IntegrationDataPackRegistrar.register(bus, MOD_ID, "Avaritia Integration AE2 Data");
        if (IntegrationRuntime.load(MOD_ID, bus, AE2Module::new)) {
            ModRun.init(bus);
            bus.addListener(AE2DataGen::gatherData);
        }
    }
}
