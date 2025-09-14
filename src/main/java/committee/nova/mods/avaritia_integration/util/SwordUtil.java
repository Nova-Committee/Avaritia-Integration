package committee.nova.mods.avaritia_integration.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.GameEvent;

import static net.minecraft.world.entity.LivingEntity.DATA_HEALTH_ID;

public class SwordUtil {

    public static void killEntity(LivingEntity living, Player player) {
        if (living.level.isClientSide)  {
            NetworkHandler.sendToServer(new ClientEntityRemovePacket(living.getId(), player.getId()));
            return;
        }
        if (living instanceof Player target) {
            if (target instanceof ServerPlayer serverPlayer) {
                serverPlayer.setHealth(0f);
            }
            return;
        }
        living.setSecondsOnFire(100);
        living.hurt(living.level.damageSources().playerAttack(player), living.getHealth());
        living.die(living.level.damageSources().playerAttack(player));
        living.gameEvent(GameEvent.ENTITY_DIE);
        living.dropAllDeathLoot(living.level.damageSources().playerAttack(player));
        living.entityData.set(DATA_HEALTH_ID,0f);
        living.setHealth(0f);
        living.setPose(Pose.DYING);
        living.getActiveEffects().clear();
    }
}
