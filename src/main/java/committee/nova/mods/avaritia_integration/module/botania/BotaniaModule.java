package committee.nova.mods.avaritia_integration.module.botania;

import committee.nova.mods.avaritia_integration.module.ModMeta;
import committee.nova.mods.avaritia_integration.module.Module;
import committee.nova.mods.avaritia_integration.module.ModuleEntry;
import committee.nova.mods.avaritia_integration.module.botania.item.block.entity.AsgardDandelionBlockEntity;
import committee.nova.mods.avaritia_integration.module.botania.item.block.entity.InfinityManaPoolBlockEntity;
import committee.nova.mods.avaritia_integration.module.botania.item.block.entity.SoarleanderBlockEntity;
import committee.nova.mods.avaritia_integration.module.botania.registry.BotaniaIntegrationBlockEntities;
import committee.nova.mods.avaritia_integration.module.botania.registry.BotaniaIntegrationBlocks;
import committee.nova.mods.avaritia_integration.module.botania.registry.BotaniaIntegrationItems;
import committee.nova.mods.avaritia_integration.module.botania.render.InfinityTinyPotatoBlockEntityRender;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.BotaniaForgeClientCapabilities;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.block_entity.BindableSpecialFlowerBlockEntity;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.client.render.block_entity.ManaPoolBlockEntityRenderer;
import vazkii.botania.client.render.block_entity.SpecialFlowerBlockEntityRenderer;
import vazkii.botania.common.lib.ResourceLocationHelper;
import vazkii.botania.forge.CapabilityUtil;

@ModuleEntry(id = BotaniaModule.MOD_ID, target = @ModMeta(BotaniaModule.MOD_ID))
public final class BotaniaModule implements Module {
    public static final String MOD_ID = "botania";

    @Override
    public void init(IEventBus registryBus) {
        BotaniaIntegrationBlocks.REGISTRY.register(registryBus);
        BotaniaIntegrationBlockEntities.REGISTRY.register(registryBus);
        BotaniaIntegrationItems.REGISTRY.register(registryBus);
    }

    @Override
    public void process() {
        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(BotaniaIntegrationBlocks.ASGARD_DANDELION.getId(), BotaniaIntegrationBlocks.POTTED_ASGARD_DANDELION);
        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(BotaniaIntegrationBlocks.SOARLEANDER.getId(), BotaniaIntegrationBlocks.POTTED_SOARLEANDER);
    }

    @Override
    public void registerEvent(IEventBus modBus, IEventBus gameBus) {
        gameBus.addGenericListener(BlockEntity.class, BotaniaModule::attachCommonCapability);
    }

    public static void attachCommonCapability(AttachCapabilitiesEvent<BlockEntity> e) {
        BlockEntity be = e.getObject();
        if (be.getType() == BotaniaIntegrationBlockEntities.INFINITY_MANA_POOL.get()) {
            e.addCapability(ResourceLocationHelper.prefix("mana_receiver"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.MANA_RECEIVER, (ManaReceiver) be));
            e.addCapability(ResourceLocationHelper.prefix("wandable"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.WANDABLE, (Wandable) be));
        }
    }

    @Override
    public void processClient() {
        BlockEntityRenderers.register(BotaniaIntegrationBlockEntities.INFINITY_MANA_POOL.get(), ManaPoolBlockEntityRenderer::new);
        BlockEntityRenderers.register(BotaniaIntegrationBlockEntities.ASGARD_DANDELION.get(), SpecialFlowerBlockEntityRenderer::new);
        BlockEntityRenderers.register(BotaniaIntegrationBlockEntities.SOARLEANDER_BLOCK_ENTITIES.get(), SpecialFlowerBlockEntityRenderer::new);
        BlockEntityRenderers.register(BotaniaIntegrationBlockEntities.INFINITY_TINY_POTATO.get(), InfinityTinyPotatoBlockEntityRender::new);
        ItemBlockRenderTypes.setRenderLayer(BotaniaIntegrationBlocks.ASGARD_DANDELION.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BotaniaIntegrationBlocks.ASGARD_DANDELION_FLOATING.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BotaniaIntegrationBlocks.SOARLEANDER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BotaniaIntegrationBlocks.SOARLEANDER_FLOATING.get(), RenderType.cutout());
    }

    @Override
    public void registerClientEvent(IEventBus modBus, IEventBus gameBus) {
        gameBus.addGenericListener(BlockEntity.class, BotaniaModule::attachClientCapability);
    }

    public static void attachClientCapability(AttachCapabilitiesEvent<BlockEntity> e) {
        BlockEntity be = e.getObject();
        if (be instanceof AsgardDandelionBlockEntity tile)
            e.addCapability(ResourceLocationHelper.prefix("wand_hud"), CapabilityUtil.makeProvider(BotaniaForgeClientCapabilities.WAND_HUD, new BindableSpecialFlowerBlockEntity.BindableFlowerWandHud<>(tile)));
        if (be instanceof SoarleanderBlockEntity tile)
            e.addCapability(ResourceLocationHelper.prefix("wand_hud"), CapabilityUtil.makeProvider(BotaniaForgeClientCapabilities.WAND_HUD, new BindableSpecialFlowerBlockEntity.BindableFlowerWandHud<>(tile)));
        if (be instanceof InfinityManaPoolBlockEntity tile)
            e.addCapability(ResourceLocationHelper.prefix("wand_hud"), CapabilityUtil.makeProvider(BotaniaForgeClientCapabilities.WAND_HUD, new InfinityManaPoolBlockEntity.WandHud(tile)));
    }

    @Override
    public void collectCreativeTabItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        output.accept(BotaniaIntegrationBlocks.ASGARD_DANDELION.get());
        output.accept(BotaniaIntegrationBlocks.ASGARD_DANDELION_FLOATING.get());
        output.accept(BotaniaIntegrationBlocks.POTTED_ASGARD_DANDELION.get());

        output.accept(BotaniaIntegrationBlocks.SOARLEANDER.get());
        output.accept(BotaniaIntegrationBlocks.SOARLEANDER_FLOATING.get());
        output.accept(BotaniaIntegrationBlocks.POTTED_SOARLEANDER.get());

        output.accept(BotaniaIntegrationBlocks.INFINITY_MANA_POOL.get());
        output.accept(BotaniaIntegrationBlocks.INFINITY_POTATO.get());
    }
}
