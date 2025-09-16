package committee.nova.mods.avaritia_integration.client;

import committee.nova.mods.avaritia_integration.client.screen.ModuleListScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class AvaritiaIntegrationClient {
    @SubscribeEvent
    public static void initClient(FMLClientSetupEvent event) {
        MinecraftForge.registerConfigScreen(ModuleListScreen::new);
    }
}
