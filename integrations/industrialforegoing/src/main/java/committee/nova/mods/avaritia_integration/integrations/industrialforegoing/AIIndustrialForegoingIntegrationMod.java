package committee.nova.mods.avaritia_integration.integrations.industrialforegoing;

import committee.nova.mods.avaritia_integration.api.load.IntegrationDataPackRegistrar;
import committee.nova.mods.avaritia_integration.api.load.IntegrationRule;
import committee.nova.mods.avaritia_integration.api.load.IntegrationRuntime;
import committee.nova.mods.avaritia_integration.integrations.industrialforegoing.datagen.IndustrialForegoingDataGen;
import committee.nova.mods.avaritia_integration.module.ModModule;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(AIIndustrialForegoingIntegrationMod.MOD_ID)
public final class AIIndustrialForegoingIntegrationMod implements ModModule {

    public static final String MOD_ID = "avaritia_integration_industrialforegoing";
    private static final String DEPENDENCY_MOD_ID = "industrialforegoing";

    public AIIndustrialForegoingIntegrationMod(IEventBus bus, ModContainer modContainer) {
        IntegrationDataPackRegistrar.register(bus, MOD_ID, "Avaritia Integration Industrial Foregoing Data");
        if (IntegrationRuntime.shouldLoad(MOD_ID, this) && IntegrationRuntime.load(MOD_ID, bus, new IndustrialForegoingModule())) {
            bus.addListener(IndustrialForegoingDataGen::gatherData);
        }
    }

    @Override
    public IntegrationRule defaultLoadRule() {
        return ModModule.rule(ModModule.dependency(DEPENDENCY_MOD_ID));
    }
}
