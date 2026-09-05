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
    static final String DEPENDENCY_MOD_ID = "botania";
    static final String MIN_DEPENDENCY_VERSION = "455-SNAPSHOT";

    public AIBotaniaIntegrationMod(IEventBus bus, ModContainer modContainer) {
        IntegrationDataPackRegistrar.register(bus, MOD_ID, "Avaritia Integration Botania Data");
        if (IntegrationRuntime.shouldLoad(MOD_ID, this)) IntegrationRuntime.load(MOD_ID, bus, new BotaniaModule());
    }

    @Override
    public IntegrationRule defaultLoadRule() {
        return createDefaultLoadRule();
    }

    static IntegrationRule createDefaultLoadRule() {
        return ModModule.rule(ModModule.dependency(DEPENDENCY_MOD_ID, MIN_DEPENDENCY_VERSION));
    }
}
