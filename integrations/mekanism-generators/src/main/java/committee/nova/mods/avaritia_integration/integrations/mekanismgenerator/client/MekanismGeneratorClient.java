package committee.nova.mods.avaritia_integration.integrations.mekanismgenerator.client;

import committee.nova.mods.avaritia_integration.integrations.mekanismgenerator.common.blockentity.InfinityAdvancedSolarGeneratorBlockEntity;
import committee.nova.mods.avaritia_integration.integrations.mekanismgenerator.common.blockentity.InfinitySolarGeneratorBlockEntity;
import committee.nova.mods.avaritia_integration.integrations.mekanismgenerator.common.blockentity.NeutronAdvancedSolarGeneratorBlockEntity;
import committee.nova.mods.avaritia_integration.integrations.mekanismgenerator.common.blockentity.NeutronSolarGeneratorBlockEntity;
import committee.nova.mods.avaritia_integration.integrations.mekanismgenerator.common.registries.GenIntegrationBlocks;
import committee.nova.mods.avaritia_integration.integrations.mekanismgenerator.common.registries.GenIntegrationContainerTypes;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import mekanism.client.ClientRegistration;
import mekanism.client.ClientRegistrationUtil;
import mekanism.client.model.baked.ExtensionBakedModel.TransformedBakedModel;
import mekanism.client.render.lib.QuadTransformation;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.generators.client.gui.GuiSolarGenerator;

public final class MekanismGeneratorClient {

    private MekanismGeneratorClient() {}

    public static void register(IEventBus modBus) {
        modBus.addListener(EventPriority.NORMAL, MekanismGeneratorClient::clientSetupEvent);
        modBus.addListener(EventPriority.NORMAL, MekanismGeneratorClient::registerScreen);
    }

    private static void clientSetupEvent(FMLClientSetupEvent event) {
        ClientRegistration.addCustomModel(GenIntegrationBlocks.NEUTRON_ADVANCED_SOLAR_GENERATOR,
                (orig, evt) -> new TransformedBakedModel<Void>(orig,
                        QuadTransformation.translate(0, 1, 0)));
        ClientRegistration.addCustomModel(GenIntegrationBlocks.INFINITY_ADVANCED_SOLAR_GENERATOR,
                (orig, evt) -> new TransformedBakedModel<Void>(orig,
                        QuadTransformation.translate(0, 1, 0)));
    }

    private static void registerScreen(RegisterMenuScreensEvent event) {
        ClientRegistrationUtil.registerScreen(event, GenIntegrationContainerTypes.INFINITY_SOLAR_GENERATOR,
                (MekanismTileContainer<InfinitySolarGeneratorBlockEntity> container, Inventory inv,
                 Component title) -> new GuiSolarGenerator<>(container, inv, title));
        ClientRegistrationUtil.registerScreen(event, GenIntegrationContainerTypes.NEUTRON_SOLAR_GENERATOR,
                (MekanismTileContainer<NeutronSolarGeneratorBlockEntity> container, Inventory inv,
                 Component title) -> new GuiSolarGenerator<>(container, inv, title));
        ClientRegistrationUtil.registerScreen(event, GenIntegrationContainerTypes.INFINITY_ADVANCED_SOLAR_GENERATOR,
                (MekanismTileContainer<InfinityAdvancedSolarGeneratorBlockEntity> container, Inventory inv,
                 Component title) -> new GuiSolarGenerator<>(container, inv, title));
        ClientRegistrationUtil.registerScreen(event, GenIntegrationContainerTypes.NEUTRON_ADVANCED_SOLAR_GENERATOR,
                (MekanismTileContainer<NeutronAdvancedSolarGeneratorBlockEntity> container, Inventory inv,
                 Component title) -> new GuiSolarGenerator<>(container, inv, title));
    }
}
