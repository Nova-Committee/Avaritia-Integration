package committee.nova.mods.avaritia_integration.integrations.slashblade.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.GameEvent;

public final class SwordUtil {

    private SwordUtil() {}

    public static void killEntity(LivingEntity living, Player player) {
        if (living.level().isClientSide) {
            return;
        }
        DamageSource source = living.damageSources().playerAttack(player);
        if (living instanceof ServerPlayer serverPlayer) {
            serverPlayer.setHealth(0f);
            return;
        }
        living.igniteForSeconds(100);
        living.hurt(source, Math.max(living.getHealth(), 1.0f));
        living.die(source);
        living.gameEvent(GameEvent.ENTITY_DIE);
        living.setHealth(0f);
        living.setPose(Pose.DYING);
        living.removeAllEffects();
    }
}
