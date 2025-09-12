package committee.nova.mods.avaritia_integration.client;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.client.render.InfinityManaPoolRender;
import committee.nova.mods.avaritia_integration.client.render.InfinityTinyPotatoBlockEntityRender;
import committee.nova.mods.avaritia_integration.common.blockentity.AsgardDandelionBlockEntity;
import committee.nova.mods.avaritia_integration.common.blockentity.InfinityManaPoolBlockEntity;
import committee.nova.mods.avaritia_integration.common.blockentity.SoarleanderBlockEntity;
import committee.nova.mods.avaritia_integration.init.registry.ModBlockEntities;
import committee.nova.mods.avaritia_integration.init.registry.ModBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import vazkii.botania.client.render.block_entity.SpecialFlowerBlockEntityRenderer;

@Mod.EventBusSubscriber(modid = AvaritiaIntegration.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AvaritiaIntegrationModClient {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            BlockEntityRenderers.register(ModBlockEntities.INFINITY_MANA_POOL.get(), InfinityManaPoolRender::new);
            BlockEntityRenderers.register(ModBlockEntities.ASGARD_DANDELION.get(), SpecialFlowerBlockEntityRenderer::new);
            BlockEntityRenderers.register(ModBlockEntities.SOARLEANDER.get(), SpecialFlowerBlockEntityRenderer::new);
            BlockEntityRenderers.register(ModBlockEntities.INFINITY_TINY_POTATO.get(), InfinityTinyPotatoBlockEntityRender::new);
       });
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.asgard_dandelion.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.asgard_dandelion_floating.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.soarleander.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.soarleander_floating.get(), RenderType.cutout());
    }

}
