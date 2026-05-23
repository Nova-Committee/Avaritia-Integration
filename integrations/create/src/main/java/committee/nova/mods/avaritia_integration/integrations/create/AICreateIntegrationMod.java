package committee.nova.mods.avaritia_integration.integrations.create;

import committee.nova.mods.avaritia_integration.api.load.IntegrationRule;
import committee.nova.mods.avaritia_integration.api.load.IntegrationRuntime;
import committee.nova.mods.avaritia_integration.module.ModModule;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(AICreateIntegrationMod.MOD_ID)
public final class AICreateIntegrationMod implements ModModule {

    public static final String MOD_ID = "avaritia_integration_create";
    private static final String DEPENDENCY_MOD_ID = "create";

    public AICreateIntegrationMod(IEventBus bus, ModContainer modContainer) {
        IntegrationRuntime.load(MOD_ID, this, bus, CreateModule::new);
    }

    @Override
    public IntegrationRule defaultLoadRule() {
        return ModModule.rule(ModModule.dependency(DEPENDENCY_MOD_ID));
    }
}
