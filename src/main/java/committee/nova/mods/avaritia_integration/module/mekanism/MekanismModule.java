package committee.nova.mods.avaritia_integration.module.mekanism;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.module.ModMeta;
import committee.nova.mods.avaritia_integration.module.Module;
import committee.nova.mods.avaritia_integration.module.ModuleEntry;
import committee.nova.mods.avaritia_integration.module.mekanism.client.gui.machine.GuiMIFactory;
import committee.nova.mods.avaritia_integration.module.mekanism.client.gui.machine.GuiNeutronCollector;
import committee.nova.mods.avaritia_integration.module.mekanism.client.gui.machine.GuiSingularityCompressor;
import committee.nova.mods.avaritia_integration.module.mekanism.common.network.to_server.MekIntegrationPacketGuiInteract;
import committee.nova.mods.avaritia_integration.module.mekanism.common.registries.*;
import committee.nova.mods.avaritia_integration.module.mekanismgenerator.common.registries.GenIntegrationBlocks;
import mekanism.client.ClientRegistrationUtil;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab.ItemDisplayParameters;
import net.minecraft.world.item.CreativeModeTab.Output;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@ModuleEntry(id = MekanismModule.MOD_ID, target = @ModMeta(MekanismModule.MOD_ID))
public final class MekanismModule implements Module {
    public static final String MOD_ID = "mekanism";

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(AvaritiaIntegration.MOD_ID, path);
    }

    @Override
    public void init(IEventBus registryBus) {
        MekIntegrationItems.ITEMS.register(registryBus);
        MekIntegrationBlocks.BLOCKS.register(registryBus);
        MekIntegrationContainerTypes.CONTAINER_TYPES.register(registryBus);
        MekIntegrationTileEntityTypes.TILE_ENTITY_TYPES.register(registryBus);
        MekIntegrationChemicals.CHEMICALS.register(registryBus);
        MekIntegrationRecipeSerializers.RECIPE_SERIALIZERS.register(registryBus);
        registryBus.addListener(this::registerPayloads);
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar(AvaritiaIntegration.MOD_ID)
                .playToServer(MekIntegrationPacketGuiInteract.TYPE, MekIntegrationPacketGuiInteract.STREAM_CODEC, MekIntegrationPacketGuiInteract::handle);
    }

    @Override
    public void registerClientEvent(IEventBus modBus, IEventBus gameBus) {
        modBus.addListener(EventPriority.LOW, this::registerScreen);
    }

    private void registerScreen(RegisterMenuScreensEvent event) {
        ClientRegistrationUtil.registerScreen(event, MekIntegrationContainerTypes.NEUTRON_COLLECTOR, GuiNeutronCollector::new);
        ClientRegistrationUtil.registerScreen(event, MekIntegrationContainerTypes.SINGULARITY_COMPRESSOR, GuiSingularityCompressor::new);
        ClientRegistrationUtil.registerScreen(event, MekIntegrationContainerTypes.FACTORY, GuiMIFactory::new);
    }

    @Override
    public void collectCreativeTabItems(ItemDisplayParameters parameters, Output output) {
        for (Holder<Item> itemHolder : MekIntegrationItems.ITEMS.getEntries()) {
            output.accept(itemHolder.value());
        }
        for (Holder<Item> blockHolder : MekIntegrationBlocks.BLOCKS.getSecondaryEntries()) {
            output.accept(blockHolder.value());
        }
    }
}
