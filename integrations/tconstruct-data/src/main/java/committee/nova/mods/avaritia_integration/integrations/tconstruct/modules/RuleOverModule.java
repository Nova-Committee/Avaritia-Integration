package committee.nova.mods.avaritia_integration.integrations.tconstruct.modules;

import committee.nova.mods.avaritia.init.registry.ModDamageTypes;
import committee.nova.mods.avaritia.util.ToolUtils;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.loadable.record.SingletonLoader;
import slimeknights.tconstruct.common.TinkerDamageTypes;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public enum RuleOverModule implements ModifierModule, MeleeDamageModifierHook, MeleeHitModifierHook {

    INSTANCE;

    private static final List<ModuleHook<?>> DEFAULT_HOOKS = HookProvider
            .<RuleOverModule>defaultHooks(ModifierHooks.MELEE_DAMAGE, ModifierHooks.MELEE_HIT);
    public static final RecordLoadable<RuleOverModule> LOADER = new SingletonLoader<>(INSTANCE);

    public @NotNull RecordLoadable<RuleOverModule> getLoader() {
        return LOADER;
    }

    public @NotNull List<ModuleHook<?>> getDefaultHooks() {
        return DEFAULT_HOOKS;
    }

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context,
                                float baseDamage, float damage) {
        LivingEntity target = context.getLivingTarget();
        LivingEntity attacker = context.getPlayerAttacker();
        if (tool.hasTag(TinkerTags.Items.MELEE_PRIMARY) && !context.isExtraAttack() && target != null &&
                attacker != null) {
            DamageSource source = TinkerDamageTypes.source(target.level().registryAccess(), ModDamageTypes.INFINITY,
                    attacker);
            if (target instanceof EnderDragon dragon) {
                dragon.hurt(dragon.head, source, Float.MAX_VALUE);
                return damage;
            } else if (target instanceof Player pvp) {
                if (ToolUtils.isInfinite(pvp)) {
                    // 玩家身着无尽甲则只造成爆炸伤害
                    context.getLevel().explode(attacker, pvp.getBlockX(), pvp.getBlockY(), pvp.getBlockZ(), 25.0F,
                            Level.ExplosionInteraction.MOB);
                    return damage;
                }
            }
            target.hurt(source, Float.MAX_VALUE);
        }
        return damage;
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context,
                              float damageDealt) {
        LivingEntity target = context.getLivingTarget();
        LivingEntity attacker = context.getPlayerAttacker();
        if (tool.hasTag(TinkerTags.Items.MELEE_PRIMARY) && !context.isExtraAttack() && target != null &&
                target.isDeadOrDying() && attacker != null) {
            target.setHealth(0);// 设置血量为零
            target.die(TinkerDamageTypes.source(target.level().registryAccess(), ModDamageTypes.INFINITY, attacker));// 修正设置死亡
            if (context.getLevel() instanceof ServerLevel level) {
                attacker.killedEntity(level, target);// 添加至信息统计
            }
        }
    }
}
