package committee.nova.mods.avaritia_integration.integrations.enderio;

import committee.nova.mods.avaritia_integration.api.load.IntegrationDataPackRegistrar;
import committee.nova.mods.avaritia_integration.api.load.IntegrationRule;
import committee.nova.mods.avaritia_integration.api.load.IntegrationRuntime;
import committee.nova.mods.avaritia_integration.integrations.enderio.datagen.EnderIODataGen;
import committee.nova.mods.avaritia_integration.module.ModModule;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(AIEnderIOIntegrationMod.MOD_ID)
public final class AIEnderIOIntegrationMod implements ModModule {

    public static final String MOD_ID = "avaritia_integration_enderio";
    private static final String DEPENDENCY_MOD_ID = "enderio";

    public AIEnderIOIntegrationMod(IEventBus bus, ModContainer modContainer) {
        IntegrationDataPackRegistrar.register(bus, MOD_ID, "Avaritia Integration Ender IO Data");
        if (IntegrationRuntime.load(MOD_ID, this, bus, EnderIOModule::new)) {
            bus.addListener(EnderIODataGen::gatherData);
        }
    }

    @Override
    public IntegrationRule defaultLoadRule() {
        return ModModule.rule(ModModule.dependency(DEPENDENCY_MOD_ID));
    }
}
