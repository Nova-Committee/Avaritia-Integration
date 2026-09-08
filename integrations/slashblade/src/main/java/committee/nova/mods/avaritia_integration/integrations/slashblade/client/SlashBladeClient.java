package committee.nova.mods.avaritia_integration.integrations.slashblade.client;

import committee.nova.mods.avaritia_integration.integrations.slashblade.registry.SlashBladeIntegrationItems;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ModelEvent;

import mods.flammpfeil.slashblade.client.ClientHandler;

public final class SlashBladeClient {

    private SlashBladeClient() {}

    public static void register(IEventBus modBus) {
        modBus.addListener(SlashBladeClient::onModifyBakingResult);
    }

    private static void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
        ClientHandler.bakeBlade(SlashBladeIntegrationItems.STREDGEUNIVERSE.get(), event);
    }
}
