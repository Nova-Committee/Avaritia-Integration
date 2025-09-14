package committee.nova.mods.avaritia_integration.common.entity;

import committee.nova.mods.avaritia_integration.util.SwordUtil;
import mods.flammpfeil.slashblade.SlashBlade.RegistryEvents;
import mods.flammpfeil.slashblade.ability.StunManager;
import mods.flammpfeil.slashblade.entity.EntityAbstractSummonedSword;
import mods.flammpfeil.slashblade.entity.Projectile;
import mods.flammpfeil.slashblade.util.KnockBacks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.PlayMessages;

public class MeteoriteSwordEntity extends EntityAbstractSummonedSword {

    public MeteoriteSwordEntity(EntityType<? extends Projectile> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
        this.setPierce((byte)1);
    }

    public void defineSynchedData() {
        super.defineSynchedData();
    }

    public static MeteoriteSwordEntity createInstance(PlayMessages.SpawnEntity packet, Level worldIn) {
        return new MeteoriteSwordEntity(RegistryEvents.StormSwords, worldIn);
    }

    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity targetEntity = entityHitResult.getEntity();
        if (targetEntity instanceof LivingEntity living && this.getOwner() instanceof Player player) {
            KnockBacks.toss.action.accept(living);
            StunManager.setStun(living);
            SwordUtil.killEntity(living, player);
        }
        super.onHitEntity(entityHitResult);
    }

    protected void onHitBlock(BlockHitResult blockraytraceresult) {
        this.burst();
    }
}