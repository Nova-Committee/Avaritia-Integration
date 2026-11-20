package committee.nova.mods.avaritia_integration.integrations.thaumaturge;

import committee.nova.mods.avaritia_integration.api.load.IntegrationDataPackRegistrar;
import committee.nova.mods.avaritia_integration.api.load.IntegrationRule;
import committee.nova.mods.avaritia_integration.api.load.IntegrationRuntime;
import committee.nova.mods.avaritia_integration.module.ModModule;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(AIThaumaturgeIntegrationMod.MOD_ID)
public final class AIThaumaturgeIntegrationMod implements ModModule {

    public static final String MOD_ID = "avaritia_integration_thaumaturge";

    public AIThaumaturgeIntegrationMod(IEventBus bus) {
        IntegrationDataPackRegistrar.register(bus, MOD_ID, "Avaritia Integration Thaumaturge Data");
        if (IntegrationRuntime.shouldLoad(MOD_ID, this)) {
            IntegrationRuntime.load(MOD_ID, bus, new ThaumaturgeModule());
        }
    }

    @Override
    public IntegrationRule defaultLoadRule() {
        return ModModule.rule(ModModule.dependency("thaumaturge", "1.1.0"));
    }
}
