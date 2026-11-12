package committee.nova.mods.avaritia_integration.integrations.tconstruct.modules;

import committee.nova.mods.avaritia.init.registry.ModItems;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.loadable.record.SingletonLoader;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.mining.BlockBreakModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.context.ToolHarvestContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

import java.util.List;
import java.util.Objects;

import static slimeknights.tconstruct.library.modifiers.Modifier.RANDOM;

public enum CondensingModule
        implements ModifierModule, MeleeHitModifierHook, BlockBreakModifierHook, ProjectileHitModifierHook {

    INSTANCE;

    private static final List<ModuleHook<?>> DEFAULT_HOOKS = HookProvider.<CondensingModule>defaultHooks(
            ModifierHooks.MELEE_HIT, ModifierHooks.BLOCK_BREAK, ModifierHooks.PROJECTILE_HIT);
    public static final RecordLoadable<CondensingModule> LOADER = new SingletonLoader<>(INSTANCE);

    public @NotNull RecordLoadable<CondensingModule> getLoader() {
        return LOADER;
    }

    public @NotNull List<ModuleHook<?>> getDefaultHooks() {
        return DEFAULT_HOOKS;
    }

    private void drop(BlockPos pos, LivingEntity living) {
        float chance = (float) (Objects.requireNonNull(living.getAttribute(Attributes.GRAVITY)).getValue() / 0.08) - 1;
        if (RANDOM.nextFloat() < chance) {
            Level level = living.level();
            ItemEntity entity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(),
                    new ItemStack(ModItems.neutron_pile.get()));
            entity.setDefaultPickUpDelay();
            level.addFreshEntity(entity);
        }
    }

    @Override
    public void afterMeleeHit(@NotNull IToolStackView tool, @NotNull ModifierEntry modifier, ToolAttackContext context,
                              float damageDealt) {
        LivingEntity living = context.getLivingTarget();
        if (context.isFullyCharged() && !context.isExtraAttack() && living != null) {
            drop(living.getOnPos(), context.getAttacker());
        }
    }

    @Override
    public void afterBlockBreak(IToolStackView tool, ModifierEntry modifier, ToolHarvestContext context) {
        if (context.isEffective()) {
            drop(context.getPos(), context.getLiving());
        }
    }

    @Override
    public boolean onProjectileHitEntity(@NotNull ModifierNBT modifiers, ModDataNBT persistentData,
                                         @NotNull ModifierEntry modifier, @NotNull Projectile projectile,
                                         EntityHitResult hit, @javax.annotation.Nullable LivingEntity attacker,
                                         @javax.annotation.Nullable LivingEntity target) {
        if (target != null && attacker != null) {
            drop(target.getOnPos(), attacker);
        }
        return false;
    }
}
