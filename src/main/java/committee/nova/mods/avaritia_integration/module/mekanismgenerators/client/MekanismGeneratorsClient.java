package committee.nova.mods.avaritia_integration.module.mekanismgenerators.client;

import committee.nova.mods.avaritia_integration.module.mekanismgenerators.common.registry.GenIntegrationBlocks;
import committee.nova.mods.avaritia_integration.module.mekanismgenerators.common.registry.GenIntegrationContainerTypes;
import committee.nova.mods.avaritia_integration.module.mekanismgenerators.common.tile.TileEntityInfinityAdvancedSolarGenerator;
import committee.nova.mods.avaritia_integration.module.mekanismgenerators.common.tile.TileEntityInfinitySolarGenerator;
import committee.nova.mods.avaritia_integration.module.mekanismgenerators.common.tile.TileEntityNeutronAdvancedSolarGenerator;
import committee.nova.mods.avaritia_integration.module.mekanismgenerators.common.tile.TileEntityNeutronSolarGenerator;
import mekanism.client.ClientRegistrationUtil;
import mekanism.client.model.baked.ExtensionBakedModel;
import mekanism.client.render.lib.QuadTransformation;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.generators.client.gui.GuiSolarGenerator;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegisterEvent;

import static mekanism.client.ClientRegistration.addCustomModel;

public final class MekanismGeneratorsClient {
    private MekanismGeneratorsClient() {}

    public static void register(IEventBus modBus) {
        modBus.addListener(MekanismGeneratorsClient::clientSetupEvent);
        modBus.addListener(EventPriority.LOW, MekanismGeneratorsClient::registerScreen);
    }

    private static void clientSetupEvent(FMLClientSetupEvent event) {
        addCustomModel(GenIntegrationBlocks.NEUTRON_ADVANCED_SOLAR_GENERATOR, (orig, evt) -> new ExtensionBakedModel.TransformedBakedModel<Void>(orig,
                QuadTransformation.translate(0, 1, 0)));
        addCustomModel(GenIntegrationBlocks.INFINITY_ADVANCED_SOLAR_GENERATOR, (orig, evt) -> new ExtensionBakedModel.TransformedBakedModel<Void>(orig,
                QuadTransformation.translate(0, 1, 0)));
    }

    private static void registerScreen(RegisterEvent event) {
        event.register(Registries.MENU, helper -> {
            ClientRegistrationUtil.registerScreen(GenIntegrationContainerTypes.NEUTRON_SOLAR_GENERATOR, (MekanismTileContainer<TileEntityNeutronSolarGenerator> container, Inventory inv, Component title) -> new GuiSolarGenerator<>(container, inv, title));
            ClientRegistrationUtil.registerScreen(GenIntegrationContainerTypes.INFINITY_SOLAR_GENERATOR, (MekanismTileContainer<TileEntityInfinitySolarGenerator> container, Inventory inv, Component title) -> new GuiSolarGenerator<>(container, inv, title));
            ClientRegistrationUtil.registerScreen(GenIntegrationContainerTypes.NEUTRON_ADVANCED_SOLAR_GENERATOR, (MekanismTileContainer<TileEntityNeutronAdvancedSolarGenerator> container, Inventory inv, Component title) -> new GuiSolarGenerator<>(container, inv, title));
            ClientRegistrationUtil.registerScreen(GenIntegrationContainerTypes.INFINITY_ADVANCED_SOLAR_GENERATOR, (MekanismTileContainer<TileEntityInfinityAdvancedSolarGenerator> container, Inventory inv, Component title) -> new GuiSolarGenerator<>(container, inv, title));
        });
    }

}
