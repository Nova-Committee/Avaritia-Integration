package committee.nova.mods.avaritia_integration.module.botania;

import committee.nova.mods.avaritia_integration.module.botania.render.InfinityManaPoolRender;
import committee.nova.mods.avaritia_integration.module.botania.render.InfinityTinyPotatoBlockEntityRender;
import committee.nova.mods.avaritia_integration.module.ModMeta;
import committee.nova.mods.avaritia_integration.module.Module;
import committee.nova.mods.avaritia_integration.module.ModuleEntry;
import committee.nova.mods.avaritia_integration.module.botania.item.block.entity.AsgardDandelionBlockEntity;
import committee.nova.mods.avaritia_integration.module.botania.item.block.entity.InfinityManaPoolBlockEntity;
import committee.nova.mods.avaritia_integration.module.botania.item.block.entity.SoarleanderBlockEntity;
import committee.nova.mods.avaritia_integration.module.botania.registry.BotaniaIntegrationBlockEntities;
import committee.nova.mods.avaritia_integration.module.botania.registry.BotaniaIntegrationBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.botania.api.BotaniaForgeClientCapabilities;
import vazkii.botania.api.block_entity.BindableSpecialFlowerBlockEntity;
import vazkii.botania.client.render.block_entity.SpecialFlowerBlockEntityRenderer;
import vazkii.botania.common.lib.ResourceLocationHelper;
import vazkii.botania.forge.CapabilityUtil;

@OnlyIn(Dist.CLIENT)
@ModuleEntry(id = BotaniaClientModule.MOD_ID + "_client", target = @ModMeta(BotaniaClientModule.MOD_ID), side = Dist.CLIENT)
public final class BotaniaClientModule implements Module {
    public static final String MOD_ID = "botania";

    @Override
    public void process() {
        BlockEntityRenderers.register(BotaniaIntegrationBlockEntities.INFINITY_MANA_POOL.get(), InfinityManaPoolRender::new);
        BlockEntityRenderers.register(BotaniaIntegrationBlockEntities.ASGARD_DANDELION.get(), SpecialFlowerBlockEntityRenderer::new);
        BlockEntityRenderers.register(BotaniaIntegrationBlockEntities.SOARLEANDER_BLOCK_ENTITIES.get(), SpecialFlowerBlockEntityRenderer::new);
        BlockEntityRenderers.register(BotaniaIntegrationBlockEntities.INFINITY_TINY_POTATO.get(), InfinityTinyPotatoBlockEntityRender::new);
        ItemBlockRenderTypes.setRenderLayer(BotaniaIntegrationBlocks.ASGARD_DANDELION.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BotaniaIntegrationBlocks.ASGARD_DANDELION_FLOATING.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BotaniaIntegrationBlocks.SOARLEANDER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BotaniaIntegrationBlocks.SOARLEANDER_FLOATING.get(), RenderType.cutout());
    }

    @Override
    public void registerEvent(IEventBus modBus, IEventBus gameBus) {
        gameBus.register(BotaniaClientModule.class);
    }

    @SubscribeEvent
    public static void attachBeCapabilities(AttachCapabilitiesEvent<BlockEntity> e) {
        //FIXME::Why not write directly into class
        BlockEntity be = e.getObject();
        if (be instanceof AsgardDandelionBlockEntity tile)
            e.addCapability(ResourceLocationHelper.prefix("wand_hud"), CapabilityUtil.makeProvider(BotaniaForgeClientCapabilities.WAND_HUD, new BindableSpecialFlowerBlockEntity.BindableFlowerWandHud<>(tile)));
        if (be instanceof SoarleanderBlockEntity tile)
            e.addCapability(ResourceLocationHelper.prefix("wand_hud"), CapabilityUtil.makeProvider(BotaniaForgeClientCapabilities.WAND_HUD, new BindableSpecialFlowerBlockEntity.BindableFlowerWandHud<>(tile)));
        if (be instanceof InfinityManaPoolBlockEntity tile)
            e.addCapability(ResourceLocationHelper.prefix("wand_hud"), CapabilityUtil.makeProvider(BotaniaForgeClientCapabilities.WAND_HUD, new InfinityManaPoolBlockEntity.WandHud(tile)));
    }
}
