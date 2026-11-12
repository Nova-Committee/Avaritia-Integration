package committee.nova.mods.avaritia_integration.integrations.tconstruct.modifiers;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileLaunchModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.tools.modifiers.ability.tool.AutosmeltModifier;

public class BlazeCrownModifier extends AutosmeltModifier implements MeleeHitModifierHook, ProjectileLaunchModifierHook,
                                ProjectileHitModifierHook {

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT, ModifierHooks.PROJECTILE_LAUNCH,
                ModifierHooks.PROJECTILE_HIT);
    }

    private void setFire(Entity target) {
        target.igniteForSeconds(target.getRemainingFireTicks() / 20 + 15);
    }

    @Override
    public float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage,
                                float baseKnockback, float knockback) {
        LivingEntity target = context.getLivingTarget();
        if (target != null && !target.isOnFire()) {
            target.setRemainingFireTicks(1);
        }
        return knockback;
    }

    @Override
    public void failedMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context,
                               float damageAttempted) {
        LivingEntity target = context.getLivingTarget();
        if (target != null && target.isOnFire()) {
            target.clearFire();
        }
    }

    @Override
    public void afterMeleeHit(@NotNull IToolStackView tool, @NotNull ModifierEntry modifier, ToolAttackContext context,
                              float damageDealt) {
        setFire(context.getTarget());
    }

    @Override
    public void onProjectileLaunch(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter,
                                   Projectile projectile, @Nullable AbstractArrow arrow, ModDataNBT persistentData,
                                   boolean primary) {
        projectile.igniteForSeconds(100);
    }

    @Override
    public boolean onProjectileHitEntity(@NotNull ModifierNBT modifiers, ModDataNBT persistentData,
                                         @NotNull ModifierEntry modifier, @NotNull Projectile projectile,
                                         EntityHitResult hit, @javax.annotation.Nullable LivingEntity attacker,
                                         @javax.annotation.Nullable LivingEntity target) {
        if (target != null) {
            setFire(target);
        }
        return false;
    }
}
