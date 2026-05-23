package committee.nova.mods.avaritia_integration.integrations.mekanism;

import committee.nova.mods.avaritia_integration.api.load.IntegrationDataPackRegistrar;
import committee.nova.mods.avaritia_integration.api.load.IntegrationRule;
import committee.nova.mods.avaritia_integration.api.load.IntegrationRuntime;
import committee.nova.mods.avaritia_integration.module.ModModule;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(AIMekanismIntegrationMod.MOD_ID)
public final class AIMekanismIntegrationMod implements ModModule {

    public static final String MOD_ID = "avaritia_integration_mekanism";
    private static final String DEPENDENCY_MOD_ID = "mekanism";

    public AIMekanismIntegrationMod(IEventBus bus, ModContainer modContainer) {
        IntegrationDataPackRegistrar.register(bus, MOD_ID, "Avaritia Integration Mekanism Data");
        if (IntegrationRuntime.shouldLoad(MOD_ID, this)) IntegrationRuntime.load(MOD_ID, bus, new MekanismModule());
    }

    @Override
    public IntegrationRule defaultLoadRule() {
        return ModModule.rule(ModModule.dependency(DEPENDENCY_MOD_ID));
    }
}
