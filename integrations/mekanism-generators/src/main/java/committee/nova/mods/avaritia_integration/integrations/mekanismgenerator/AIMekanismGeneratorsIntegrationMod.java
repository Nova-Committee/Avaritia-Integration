package committee.nova.mods.avaritia_integration.integrations.mekanismgenerator;

import committee.nova.mods.avaritia_integration.api.load.IntegrationRuntime;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(AIMekanismGeneratorsIntegrationMod.MOD_ID)
public final class AIMekanismGeneratorsIntegrationMod {

    public static final String MOD_ID = "avaritia_integration_mekanism_generators";

    public AIMekanismGeneratorsIntegrationMod(IEventBus bus, ModContainer modContainer) {
        IntegrationRuntime.load(MOD_ID, bus, MekanismGeneratorModule::new);
    }
}
