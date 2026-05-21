package committee.nova.mods.avaritia_integration.integrations.botania;

import committee.nova.mods.avaritia_integration.api.load.IntegrationRuntime;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(AIBotaniaIntegrationMod.MOD_ID)
public final class AIBotaniaIntegrationMod {

    public static final String MOD_ID = "avaritia_integration_botania";

    public AIBotaniaIntegrationMod(IEventBus bus, ModContainer modContainer) {
        IntegrationRuntime.load(MOD_ID, bus, BotaniaModule::new);
    }
}
