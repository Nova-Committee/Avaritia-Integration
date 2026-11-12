package committee.nova.mods.avaritia_integration.integrations.tconstruct.modifiers;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ToolDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.mining.BreakSpeedModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class CrystalshineModifier extends Modifier implements ConditionalStatModifierHook, BreakSpeedModifierHook,
                                  ToolDamageModifierHook {

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.CONDITIONAL_STAT, ModifierHooks.BREAK_SPEED, ModifierHooks.TOOL_DAMAGE);
    }

    @Override
    public float modifyStat(IToolStackView tool, ModifierEntry modifier, LivingEntity living, FloatToolStat stat,
                            float baseValue, float multiplier) {
        if (stat == ToolStats.VELOCITY && tool.getDamage() == 0) {
            return (float) (baseValue * (1 + 0.6 * modifier.getLevel()));
        }
        return baseValue;
    }

    @Override
    public void onBreakSpeed(IToolStackView tool, ModifierEntry modifier, PlayerEvent.BreakSpeed event,
                             Direction sideHit, boolean isEffective, float miningSpeedModifier) {
        if (!isEffective || event.getEntity() == null || tool.getDamage() > 0) {
            return;
        }
        event.setNewSpeed((float) (event.getNewSpeed() * (1 + 0.6 * modifier.getLevel())));
    }

    @Override
    public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity holder) {
        if (tool.getDamage() == 0) {
            int maxDamage = amount;
            for (int i = 0; i < maxDamage; i++) {
                if (RANDOM.nextFloat() < 0.6) {
                    amount--;
                }
            }
        }
        return amount;
    }
}
