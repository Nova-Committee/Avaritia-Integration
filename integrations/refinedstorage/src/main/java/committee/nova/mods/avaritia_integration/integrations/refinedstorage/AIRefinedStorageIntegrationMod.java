package committee.nova.mods.avaritia_integration.integrations.refinedstorage;

import committee.nova.mods.avaritia_integration.api.load.IntegrationRule;
import committee.nova.mods.avaritia_integration.api.load.IntegrationRuntime;
import committee.nova.mods.avaritia_integration.module.ModModule;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(AIRefinedStorageIntegrationMod.MOD_ID)
public final class AIRefinedStorageIntegrationMod implements ModModule {

    public static final String MOD_ID = "avaritia_integration_refinedstorage";
    private static final String DEPENDENCY_MOD_ID = "refinedstorage";

    public AIRefinedStorageIntegrationMod(IEventBus bus, ModContainer modContainer) {
        if (IntegrationRuntime.shouldLoad(MOD_ID, this))
            IntegrationRuntime.load(MOD_ID, bus, new RefinedStorageModule());
    }

    @Override
    public IntegrationRule defaultLoadRule() {
        return ModModule.rule(ModModule.dependency(DEPENDENCY_MOD_ID));
    }
}
