package committee.nova.mods.avaritia_integration.integrations.pneumaticcraft;

import committee.nova.mods.avaritia_integration.api.load.IntegrationRule;
import committee.nova.mods.avaritia_integration.api.load.IntegrationRuntime;
import committee.nova.mods.avaritia_integration.module.ModModule;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(AIPneumaticCraftIntegrationMod.MOD_ID)
public final class AIPneumaticCraftIntegrationMod implements ModModule {

    public static final String MOD_ID = "avaritia_integration_pneumaticcraft";
    private static final String DEPENDENCY_MOD_ID = "pneumaticcraft";

    public AIPneumaticCraftIntegrationMod(IEventBus bus, ModContainer modContainer) {
        if (IntegrationRuntime.shouldLoad(MOD_ID, this) && IntegrationRuntime.load(MOD_ID, bus,
                new PneumaticCraftModule())) ModRun.init(bus);
    }

    @Override
    public IntegrationRule defaultLoadRule() {
        return ModModule.rule(ModModule.dependency(DEPENDENCY_MOD_ID));
    }
}
