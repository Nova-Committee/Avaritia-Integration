package committee.nova.mods.avaritia_integration.init.handler;

import net.minecraft.world.entity.animal.Chicken;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @author: cnlimiter
 */
@Mod.EventBusSubscriber
public class EntityHandler {
    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (event.getEntity() instanceof Chicken) {
            if (event.getSource() == event.getEntity().level().damageSources().fellOutOfWorld()) {
                event.getDrops().clear();
            }
        }
    }
}
