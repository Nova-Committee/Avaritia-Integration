package committee.nova.mods.avaritia_integration.util;

import committee.nova.mods.avaritia_integration.client.render.InfinityManaPoolRender;
import committee.nova.mods.avaritia_integration.client.render.InfinityTinyPotatoBlockEntityRender;
import committee.nova.mods.avaritia_integration.common.blockentity.AsgardDandelionBlockEntity;
import committee.nova.mods.avaritia_integration.common.blockentity.InfinityManaPoolBlockEntity;
import committee.nova.mods.avaritia_integration.common.blockentity.SoarleanderBlockEntity;
import committee.nova.mods.avaritia_integration.init.registry.BotaniaReg;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.BotaniaForgeClientCapabilities;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.block_entity.BindableSpecialFlowerBlockEntity;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.client.render.block_entity.SpecialFlowerBlockEntityRenderer;
import vazkii.botania.common.lib.ResourceLocationHelper;
import vazkii.botania.forge.CapabilityUtil;

/**
 * @author: cnlimiter
 */
public class BotaniaUtils {
    public static void attachBeCapabilities(AttachCapabilitiesEvent<BlockEntity> e) {
        BlockEntity be = e.getObject();
        if (be instanceof InfinityManaPoolBlockEntity) {
            e.addCapability(ResourceLocationHelper.prefix("mana_receiver"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.MANA_RECEIVER, (ManaReceiver) be));
            e.addCapability(ResourceLocationHelper.prefix("wandable"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.WANDABLE, (Wandable) be));
        }
        if (be instanceof AsgardDandelionBlockEntity tile) {
            e.addCapability(ResourceLocationHelper.prefix("wand_hud"),
                    CapabilityUtil.makeProvider(BotaniaForgeClientCapabilities.WAND_HUD, new BindableSpecialFlowerBlockEntity.BindableFlowerWandHud(tile))
            );
        }
        if (be instanceof SoarleanderBlockEntity tile) {
            e.addCapability(ResourceLocationHelper.prefix("wand_hud"),
                    CapabilityUtil.makeProvider(BotaniaForgeClientCapabilities.WAND_HUD, new BindableSpecialFlowerBlockEntity.BindableFlowerWandHud(tile))
            );
        }
        if (be instanceof InfinityManaPoolBlockEntity tile) {
            e.addCapability(ResourceLocationHelper.prefix("wand_hud"),
                    CapabilityUtil.makeProvider(BotaniaForgeClientCapabilities.WAND_HUD, new InfinityManaPoolBlockEntity.WandHud(tile))
            );
        }
    }
    public static void onClientSetup() {
        BlockEntityRenderers.register(BotaniaReg.INFINITY_MANA_POOL.get(), InfinityManaPoolRender::new);
        BlockEntityRenderers.register(BotaniaReg.ASGARD_DANDELION.get(), SpecialFlowerBlockEntityRenderer::new);
        BlockEntityRenderers.register(BotaniaReg.SOARLEANDER_BLOCK_ENTITIES.get(), SpecialFlowerBlockEntityRenderer::new);
        BlockEntityRenderers.register(BotaniaReg.INFINITY_TINY_POTATO.get(), InfinityTinyPotatoBlockEntityRender::new);
        ItemBlockRenderTypes.setRenderLayer(BotaniaReg.asgard_dandelion.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BotaniaReg.asgard_dandelion_floating.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BotaniaReg.soarleander.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BotaniaReg.soarleander_floating.get(), RenderType.cutout());
    }
}
