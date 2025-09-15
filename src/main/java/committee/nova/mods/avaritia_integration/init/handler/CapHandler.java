package committee.nova.mods.avaritia_integration.init.handler;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @author: cnlimiter
 */
@Mod.EventBusSubscriber
public class CapHandler {
    @SubscribeEvent
    public static void attachBeCaps(AttachCapabilitiesEvent<BlockEntity> e) {
//        IntegrationExecutor.runWhenLoad("botania", () -> () -> {
//            BotaniaUtils.attachBeCapabilities(e);
//        });
    }
}
