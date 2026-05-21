package committee.nova.mods.avaritia_integration.client;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.client.screen.ModuleListScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@EventBusSubscriber(modid = AvaritiaIntegration.MOD_ID,value = Dist.CLIENT)
public class AvaritiaIntegrationClient {
    @SubscribeEvent
    public static void clientSetUp(FMLClientSetupEvent event) {
        ModList.get().getModContainerById(AvaritiaIntegration.MOD_ID).orElseThrow().registerExtensionPoint(IConfigScreenFactory.class,
                (container, last) -> new ModuleListScreen(last));
    }
}
