package committee.nova.mods.avaritia_integration.init.handler;

import net.minecraft.world.entity.animal.Chicken;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

/**
 * @author: cnlimiter
 */
@EventBusSubscriber
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
