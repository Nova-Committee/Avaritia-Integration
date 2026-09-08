package committee.nova.mods.avaritia_integration.integrations.mysticalagriculture;

import committee.nova.mods.avaritia_integration.api.load.IntegrationDataPackRegistrar;
import committee.nova.mods.avaritia_integration.api.load.IntegrationRule;
import committee.nova.mods.avaritia_integration.api.load.IntegrationRuntime;
import committee.nova.mods.avaritia_integration.module.ModModule;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(AIMysticalAgricultureIntegrationMod.MOD_ID)
public final class AIMysticalAgricultureIntegrationMod implements ModModule {

    public static final String MOD_ID = "avaritia_integration_mysticalagriculture";

    public static final String DEPENDENCY_MOD_ID = "mysticalagriculture";

    public static final String AGRADDITIONS_MOD_ID = "mysticalagradditions";

    public AIMysticalAgricultureIntegrationMod(IEventBus bus, ModContainer modContainer) {
        IntegrationDataPackRegistrar.register(bus, MOD_ID, "Avaritia Integration Mystical Agriculture Data");
        if (IntegrationRuntime.shouldLoad(MOD_ID, this)) {
            IntegrationRuntime.load(MOD_ID, bus, new MysticalAgricultureModule());
        }
    }

    @Override
    public IntegrationRule defaultLoadRule() {
        return ModModule.rule(ModModule.dependency(DEPENDENCY_MOD_ID),
                ModModule.dependency(AGRADDITIONS_MOD_ID));
    }
}
