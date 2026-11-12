package committee.nova.mods.avaritia_integration.integrations.tconstruct.modifiers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.EquipmentChangeModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import java.util.List;

import static net.minecraft.nbt.Tag.TAG_INT;

public class CrystaluixModifier extends Modifier implements InventoryTickModifierHook, EquipmentChangeModifierHook {

    public static final ResourceLocation CrystaluixKey = ResourceLocation.withDefaultNamespace("nearest_crystal");

    @Override
    protected void registerHooks(ModuleHookMap.@NotNull Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK, ModifierHooks.EQUIPMENT_CHANGE);
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder,
                                int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (!world.isClientSide && holder.isAlive()) {
            checkCrystals(holder, modifier.getLevel());
        }
    }

    @Override
    public void onUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        LivingEntity living = context.getEntity();
        IToolStackView newTool = context.getReplacementTool();
        if (newTool == null || newTool.isBroken() || newTool.getModifier(this).getLevel() < modifier.getLevel()) {
            removeCrystals(living);
        }
    }

    private void checkCrystals(LivingEntity living, int level) {
        ModDataNBT data = PersistentDataCapability.getOrWarn(living);
        EndCrystal nearestCrystal = getCrystals(living);
        if (nearestCrystal != null) {
            if (nearestCrystal.isRemoved()) {
                removeCrystals(living);
            } else {
                nearestCrystal.setBeamTarget(living.getOnPos().below());
                if (living.tickCount % 10 == 0 && living.getHealth() < living.getMaxHealth()) {
                    living.setHealth(living.getHealth() + level);
                    living.level().playSound(null, living.getX(), living.getY(), living.getZ(),
                            SoundEvents.BEACON_AMBIENT, SoundSource.BLOCKS, 1.0f, 1.0f);
                }
            }
        }
        List<EndCrystal> list = living.level().getEntitiesOfClass(EndCrystal.class,
                living.getBoundingBox().inflate(32.0F));
        EndCrystal endcrystal = null;
        double d0 = Double.MAX_VALUE;
        for (EndCrystal endcrystal1 : list) {
            double d1 = endcrystal1.distanceToSqr(living);
            if (d1 < d0) {
                d0 = d1;
                endcrystal = endcrystal1;
            }
        }
        if (endcrystal != nearestCrystal) {
            if (endcrystal == null) {
                removeCrystals(living);
                return;
            }
            data.putInt(CrystaluixKey, endcrystal.getId());
            living.level().playSound(null, living.getX(), living.getY(), living.getZ(), SoundEvents.BEACON_ACTIVATE,
                    SoundSource.BLOCKS, 1.5f, 1.0f);
        }
    }

    private EndCrystal getCrystals(LivingEntity living) {
        ModDataNBT data = PersistentDataCapability.getOrWarn(living);
        EndCrystal nearestCrystal = null;
        if (data.contains(CrystaluixKey, TAG_INT)) {
            int id = data.getInt(CrystaluixKey);
            nearestCrystal = living.level().getEntity(id) instanceof EndCrystal crystal ? crystal : null;
        }
        return nearestCrystal;
    }

    private void removeCrystals(LivingEntity living) {
        ModDataNBT data = PersistentDataCapability.getOrWarn(living);
        EndCrystal nearestCrystal = getCrystals(living);
        if (nearestCrystal != null) {
            nearestCrystal.setBeamTarget(null);
        }
        data.remove(CrystaluixKey);
        living.level().playSound(null, living.getX(), living.getY(), living.getZ(), SoundEvents.BEACON_DEACTIVATE,
                SoundSource.BLOCKS, 1.5f, 1.0f);
    }
}
