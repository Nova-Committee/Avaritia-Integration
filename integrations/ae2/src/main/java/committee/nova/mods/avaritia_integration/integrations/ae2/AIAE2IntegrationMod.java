package committee.nova.mods.avaritia_integration.integrations.ae2;

import committee.nova.mods.avaritia_integration.api.load.IntegrationDataPackRegistrar;
import committee.nova.mods.avaritia_integration.api.load.IntegrationRule;
import committee.nova.mods.avaritia_integration.api.load.IntegrationRuntime;
import committee.nova.mods.avaritia_integration.module.ModModule;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(AIAE2IntegrationMod.MOD_ID)
public final class AIAE2IntegrationMod implements ModModule {

    public static final String MOD_ID = "avaritia_integration_ae2";

    public static final String DEPENDENCY_MOD_ID = "ae2";

    public AIAE2IntegrationMod(IEventBus bus, ModContainer modContainer) {
        IntegrationDataPackRegistrar.register(bus, MOD_ID, "Avaritia Integration AE2 Data");
        if (IntegrationRuntime.shouldLoad(MOD_ID, this) && IntegrationRuntime.load(MOD_ID, bus, new AE2Module())) {
            ModRun.init(bus);
        }
    }

    @Override
    public IntegrationRule defaultLoadRule() {
        return ModModule.rule(ModModule.dependency(DEPENDENCY_MOD_ID));
    }
}
