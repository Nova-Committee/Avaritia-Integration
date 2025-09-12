package committee.nova.mods.avaritia_integration.client;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.client.render.InfinityManaPoolRender;
import committee.nova.mods.avaritia_integration.client.render.InfinityTinyPotatoBlockEntityRender;
import committee.nova.mods.avaritia_integration.init.registry.ModBlockEntities;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = AvaritiaIntegration.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AvaritiaIntegrationModClient {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            BlockEntityRenderers.register(ModBlockEntities.INFINITY_MANA_POOL.get(), InfinityManaPoolRender::new);
            BlockEntityRenderers.register(ModBlockEntities.INFINITY_TINY_POTATO.get(), InfinityTinyPotatoBlockEntityRender::new);
       });
    }
}
