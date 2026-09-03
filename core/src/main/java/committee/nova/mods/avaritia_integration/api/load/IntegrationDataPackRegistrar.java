package committee.nova.mods.avaritia_integration.api.load;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.AddPackFindersEvent;

public final class IntegrationDataPackRegistrar {

    public static final String SERVER_DATA_PACK_PATH = "integration_packs/server_data";

    private IntegrationDataPackRegistrar() {}

    public static void register(IEventBus modBus, String integrationModId, String displayName) {
        modBus.addListener((AddPackFindersEvent event) -> {
            if (event.getPackType() != PackType.SERVER_DATA) {
                return;
            }
            if (!IntegrationLoadApi.shouldLoad(integrationModId)) {
                return;
            }
            event.addPackFinders(
                    ResourceLocation.fromNamespaceAndPath(integrationModId, SERVER_DATA_PACK_PATH),
                    PackType.SERVER_DATA,
                    Component.literal(displayName),
                    PackSource.DEFAULT,
                    true,
                    Pack.Position.TOP);
        });
    }
}
