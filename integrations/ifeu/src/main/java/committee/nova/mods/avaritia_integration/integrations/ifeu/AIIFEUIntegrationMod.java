package committee.nova.mods.avaritia_integration.integrations.ifeu;

import committee.nova.mods.avaritia_integration.api.load.IntegrationDataPackRegistrar;
import committee.nova.mods.avaritia_integration.api.load.IntegrationRuntime;
import committee.nova.mods.avaritia_integration.integrations.ifeu.datagen.IFEUDataGen;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(AIIFEUIntegrationMod.MOD_ID)
public final class AIIFEUIntegrationMod {

    public static final String MOD_ID = "avaritia_integration_ifeu";

    public AIIFEUIntegrationMod(IEventBus bus, ModContainer modContainer) {
        IntegrationDataPackRegistrar.register(bus, MOD_ID, "Avaritia Integration IFEU Data");
        if (IntegrationRuntime.load(MOD_ID, bus, IFEUModule::new)) {
            bus.addListener(IFEUDataGen::gatherData);
        }
    }
}
