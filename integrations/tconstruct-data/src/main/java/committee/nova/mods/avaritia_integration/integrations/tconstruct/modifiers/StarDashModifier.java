package committee.nova.mods.avaritia_integration.integrations.tconstruct.modifiers;

import committee.nova.mods.avaritia_integration.init.registry.AIFluids;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidStack;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.item.ranged.ModifiableLauncherItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.entity.FluidEffectProjectile;

public class StarDashModifier extends Modifier implements MeleeDamageModifierHook {

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE);
    }

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context,
                                float baseDamage, float damage) {
        LivingEntity living = context.getAttacker();
        Level level = context.getLevel();
        if (RANDOM.nextFloat() > getBonus(living)) {
            int shots = 1 + 2 * modifier.getLevel();
            float startAngle = ModifiableLauncherItem.getAngleStart(shots);
            for (int shotIndex = 0; shotIndex < modifier.getLevel() * 2 + 1; shotIndex++) {
                FluidEffectProjectile spit = new FluidEffectProjectile(level, living,
                        new FluidStack(AIFluids.source_molten_star.get().getSource(), 50), modifier.getLevel());
                Vec3 upVector = living.getUpVector(1.0f);
                float angle = startAngle + (10 * shotIndex);
                Vector3f targetVector = living.getViewVector(1.0f).toVector3f().rotate(
                        (new Quaternionf()).setAngleAxis(angle * Math.PI / 180F, upVector.x, upVector.y, upVector.z));
                spit.shoot(targetVector.x(), targetVector.y(), targetVector.z(), 3, 0.1F);
                level.addFreshEntity(spit);
            }
        } else level.explode(living, living.getX(), living.getY(), living.getZ(), modifier.getLevel(),
                Level.ExplosionInteraction.MOB);
        return damage * (1 + getBonus(living) / 4);
    }

    private static float getBonus(LivingEntity living) {
        // temperature ranges from -1.25 to 1.25, so make it go -1 to 1
        // negative is cold, positive is hot
        BlockPos pos = living.getOnPos();
        return (living.level().getBiome(pos).value().getTemperature(pos) + 1.25f) / 2.5f;
    }
}
