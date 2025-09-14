package committee.nova.mods.avaritia_integration.client;

import com.iafenvoy.integration.IntegrationExecutor;
import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.client.render.InfinityManaPoolRender;
import committee.nova.mods.avaritia_integration.client.render.InfinityTinyPotatoBlockEntityRender;
import committee.nova.mods.avaritia_integration.init.registry.BotaniaReg;
import committee.nova.mods.avaritia_integration.init.registry.ModItems;
import mods.flammpfeil.slashblade.client.renderer.model.BladeModel;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.client.render.block_entity.SpecialFlowerBlockEntityRenderer;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = AvaritiaIntegration.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AvaritiaIntegrationModClient {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        IntegrationExecutor.runWhenLoad("botania",()->()->{
            BlockEntityRenderers.register(BotaniaReg.INFINITY_MANA_POOL.get(), InfinityManaPoolRender::new);
            BlockEntityRenderers.register(BotaniaReg.ASGARD_DANDELION.get(), SpecialFlowerBlockEntityRenderer::new);
            BlockEntityRenderers.register(BotaniaReg.SOARLEANDER_BLOCK_ENTITIES.get(), SpecialFlowerBlockEntityRenderer::new);
            BlockEntityRenderers.register(BotaniaReg.INFINITY_TINY_POTATO.get(), InfinityTinyPotatoBlockEntityRender::new);
            ItemBlockRenderTypes.setRenderLayer(BotaniaReg.asgard_dandelion.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(BotaniaReg.asgard_dandelion_floating.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(BotaniaReg.soarleander.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(BotaniaReg.soarleander_floating.get(), RenderType.cutout());
        });
    }

    @SubscribeEvent
    public static void Baked(final ModelEvent.ModifyBakingResult event) {
        ModelResourceLocation loc2 = new ModelResourceLocation(
                Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(ModItems.STREDGEUNIVERSE.get())), "inventory");
        BladeModel model2 = new BladeModel(event.getModels().get(loc2), event.getModelBakery());
        event.getModels().put(loc2, model2);
    }

}
