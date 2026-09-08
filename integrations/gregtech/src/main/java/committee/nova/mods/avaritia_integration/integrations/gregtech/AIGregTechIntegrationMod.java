package committee.nova.mods.avaritia_integration.integrations.gregtech;

import committee.nova.mods.avaritia_integration.api.load.IntegrationRule;
import committee.nova.mods.avaritia_integration.api.load.IntegrationRuntime;
import committee.nova.mods.avaritia_integration.module.ModModule;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(AIGregTechIntegrationMod.MOD_ID)
public final class AIGregTechIntegrationMod implements ModModule {

    public static final String MOD_ID = "avaritia_integration_gregtech";
    public static final String DEPENDENCY_MOD_ID = "gtceu";

    public AIGregTechIntegrationMod(IEventBus bus, ModContainer modContainer) {
        if (IntegrationRuntime.shouldLoad(MOD_ID, this)) {
            IntegrationRuntime.load(MOD_ID, bus, new GregTechModule());
        }
    }

    @Override
    public IntegrationRule defaultLoadRule() {
        return ModModule.rule(ModModule.dependency(DEPENDENCY_MOD_ID));
    }
}
