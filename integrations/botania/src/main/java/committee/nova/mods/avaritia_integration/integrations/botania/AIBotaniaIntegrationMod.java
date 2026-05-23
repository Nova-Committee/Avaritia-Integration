package committee.nova.mods.avaritia_integration.integrations.botania;

import committee.nova.mods.avaritia_integration.api.load.IntegrationDataPackRegistrar;
import committee.nova.mods.avaritia_integration.api.load.IntegrationRule;
import committee.nova.mods.avaritia_integration.api.load.IntegrationRuntime;
import committee.nova.mods.avaritia_integration.module.ModModule;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(AIBotaniaIntegrationMod.MOD_ID)
public final class AIBotaniaIntegrationMod implements ModModule {

    public static final String MOD_ID = "avaritia_integration_botania";
    private static final String DEPENDENCY_MOD_ID = "botania";

    public AIBotaniaIntegrationMod(IEventBus bus, ModContainer modContainer) {
        IntegrationDataPackRegistrar.register(bus, MOD_ID, "Avaritia Integration Botania Data");
        IntegrationRuntime.load(MOD_ID, this, bus, BotaniaModule::new);
    }

    @Override
    public IntegrationRule defaultLoadRule() {
        return ModModule.rule(ModModule.dependency(DEPENDENCY_MOD_ID));
    }
}
