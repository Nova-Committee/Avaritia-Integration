package committee.nova.mods.avaritia_integration.client;

import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

//@Mod.EventBusSubscriber(modid = AvaritiaIntegration.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AvaritiaIntegrationModClient {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        //IntegrationExecutor.runWhenLoad("botania",()-> BotaniaUtils::onClientSetup);
    }

    @SubscribeEvent
    public static void Baked(final ModelEvent.ModifyBakingResult event) {
//        ModelResourceLocation loc2 = new ModelResourceLocation(
//                Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(CoreReg.stredgeuniverse)), "inventory");
//        BladeModel model2 = new BladeModel(event.getModels().get(loc2), event.getModelBakery());
//        event.getModels().put(loc2, model2);
    }

}
