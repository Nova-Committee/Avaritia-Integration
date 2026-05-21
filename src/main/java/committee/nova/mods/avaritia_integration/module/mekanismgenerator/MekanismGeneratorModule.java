package committee.nova.mods.avaritia_integration.module.mekanismgenerator;

import committee.nova.mods.avaritia_integration.module.ModMeta;
import committee.nova.mods.avaritia_integration.module.Module;
import committee.nova.mods.avaritia_integration.module.ModuleEntry;
import committee.nova.mods.avaritia_integration.module.mekanismgenerator.common.blockentity.InfinityAdvancedSolarGeneratorBlockEntity;
import committee.nova.mods.avaritia_integration.module.mekanismgenerator.common.blockentity.InfinitySolarGeneratorBlockEntity;
import committee.nova.mods.avaritia_integration.module.mekanismgenerator.common.blockentity.NeutronAdvancedSolarGeneratorBlockEntity;
import committee.nova.mods.avaritia_integration.module.mekanismgenerator.common.blockentity.NeutronSolarGeneratorBlockEntity;
import committee.nova.mods.avaritia_integration.module.mekanismgenerator.common.registries.GenIntegrationBlocks;
import committee.nova.mods.avaritia_integration.module.mekanismgenerator.common.registries.GenIntegrationContainerTypes;
import committee.nova.mods.avaritia_integration.module.mekanismgenerator.common.registries.GenIntegrationItems;
import committee.nova.mods.avaritia_integration.module.mekanismgenerator.common.registries.GenIntegrationBlockEntityTypes;
import mekanism.client.ClientRegistration;
import mekanism.client.ClientRegistrationUtil;
import mekanism.client.model.baked.ExtensionBakedModel.TransformedBakedModel;
import mekanism.client.render.lib.QuadTransformation;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.generators.client.gui.GuiSolarGenerator;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.CreativeModeTab.ItemDisplayParameters;
import net.minecraft.world.item.CreativeModeTab.Output;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@ModuleEntry(id = MekanismGeneratorModule.MOD_ID, target = @ModMeta(MekanismGeneratorModule.MOD_ID))
public class MekanismGeneratorModule implements Module {

    public static final String MOD_ID = "mekanismgenerators";

    @Override
    public void init(IEventBus registryBus) {
        GenIntegrationItems.ITEMS.register(registryBus);
        GenIntegrationBlocks.BLOCKS.register(registryBus);
        GenIntegrationContainerTypes.CONTAINER_TYPES.register(registryBus);
        GenIntegrationBlockEntityTypes.TILE_ENTITY_TYPES.register(registryBus);
    }

    @Override
    public void registerClientEvent(IEventBus modBus, IEventBus gameBus) {
        modBus.addListener(EventPriority.NORMAL, this::clientSetupEvent);
        modBus.addListener(EventPriority.NORMAL, this::registerScreen);
    }

    private void clientSetupEvent(FMLClientSetupEvent event) {
        ClientRegistration.addCustomModel(GenIntegrationBlocks.NEUTRON_ADVANCED_SOLAR_GENERATOR, (orig, evt) -> new TransformedBakedModel<Void>(orig,
                QuadTransformation.translate(0, 1, 0)));
        ClientRegistration.addCustomModel(GenIntegrationBlocks.INFINITY_ADVANCED_SOLAR_GENERATOR, (orig, evt) -> new TransformedBakedModel<Void>(orig,
                QuadTransformation.translate(0, 1, 0)));
    }

    private void registerScreen(RegisterMenuScreensEvent event) {
        ClientRegistrationUtil.registerScreen(event, GenIntegrationContainerTypes.INFINITY_SOLAR_GENERATOR, (MekanismTileContainer<InfinitySolarGeneratorBlockEntity> container, Inventory inv, Component title) -> new GuiSolarGenerator<>(container, inv, title));
        ClientRegistrationUtil.registerScreen(event, GenIntegrationContainerTypes.NEUTRON_SOLAR_GENERATOR, (MekanismTileContainer<NeutronSolarGeneratorBlockEntity> container, Inventory inv, Component title) -> new GuiSolarGenerator<>(container, inv, title));
        ClientRegistrationUtil.registerScreen(event, GenIntegrationContainerTypes.INFINITY_ADVANCED_SOLAR_GENERATOR, (MekanismTileContainer<InfinityAdvancedSolarGeneratorBlockEntity> container, Inventory inv, Component title) -> new GuiSolarGenerator<>(container, inv, title));
        ClientRegistrationUtil.registerScreen(event, GenIntegrationContainerTypes.NEUTRON_ADVANCED_SOLAR_GENERATOR, (MekanismTileContainer<NeutronAdvancedSolarGeneratorBlockEntity> container, Inventory inv, Component title) -> new GuiSolarGenerator<>(container, inv, title));
    }

    @Override
    public void collectCreativeTabItems(ItemDisplayParameters parameters, Output output) {
        for (Holder<Item> itemHolder : GenIntegrationItems.ITEMS.getEntries()) {
            output.accept(itemHolder.value());
        }
        for (Holder<Item> blockHolder : GenIntegrationBlocks.BLOCKS.getSecondaryEntries()) {
            output.accept(blockHolder.value());
        }
    }
}
