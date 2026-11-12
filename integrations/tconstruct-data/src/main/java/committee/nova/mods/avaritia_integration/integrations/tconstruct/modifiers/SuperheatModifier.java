package committee.nova.mods.avaritia_integration.integrations.tconstruct.modifiers;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;

import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.entity.ProjectileWithPower;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

public class SuperheatModifier extends Modifier implements ProjectileHitModifierHook {

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.PROJECTILE_HIT);
    }

    @Override
    public boolean onProjectileHitEntity(@NotNull ModifierNBT modifiers, ModDataNBT persistentData,
                                         @NotNull ModifierEntry modifier, @NotNull Projectile projectile,
                                         EntityHitResult hit, @javax.annotation.Nullable LivingEntity attacker,
                                         @javax.annotation.Nullable LivingEntity target) {
        if (target != null && target.isOnFire()) {
            float x = (float) (1 + 0.3 * modifier.getLevel());
            if (projectile instanceof AbstractArrow arrow) {
                arrow.setBaseDamage(arrow.getBaseDamage() * x);
                return false;
            }
            if (projectile instanceof ProjectileWithPower) {
                ((ProjectileWithPower) projectile).setPower(((ProjectileWithPower) projectile).getPower() * x);
            }
        }
        return false;
    }
}
