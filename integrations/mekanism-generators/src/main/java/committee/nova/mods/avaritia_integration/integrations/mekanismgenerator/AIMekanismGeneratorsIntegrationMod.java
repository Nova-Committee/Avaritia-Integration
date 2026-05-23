package committee.nova.mods.avaritia_integration.integrations.mekanismgenerator;

import committee.nova.mods.avaritia_integration.api.load.IntegrationDataPackRegistrar;
import committee.nova.mods.avaritia_integration.api.load.IntegrationRule;
import committee.nova.mods.avaritia_integration.api.load.IntegrationRuntime;
import committee.nova.mods.avaritia_integration.module.ModModule;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(AIMekanismGeneratorsIntegrationMod.MOD_ID)
public final class AIMekanismGeneratorsIntegrationMod implements ModModule {

    public static final String MOD_ID = "avaritia_integration_mekanism_generators";
    private static final String MEKANISM_MOD_ID = "mekanism";
    private static final String DEPENDENCY_MOD_ID = "mekanismgenerators";

    public AIMekanismGeneratorsIntegrationMod(IEventBus bus, ModContainer modContainer) {
        IntegrationDataPackRegistrar.register(bus, MOD_ID, "Avaritia Integration Mekanism Generators Data");
        IntegrationRuntime.load(MOD_ID, this, bus, MekanismGeneratorModule::new);
    }

    @Override
    public IntegrationRule defaultLoadRule() {
        return ModModule.rule(ModModule.dependency(MEKANISM_MOD_ID), ModModule.dependency(DEPENDENCY_MOD_ID));
    }
}
