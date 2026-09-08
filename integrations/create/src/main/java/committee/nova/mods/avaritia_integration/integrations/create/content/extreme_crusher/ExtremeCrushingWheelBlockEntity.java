package committee.nova.mods.avaritia_integration.integrations.create.content.extreme_crusher;

import com.simibubi.create.AllDamageTypes;
import com.simibubi.create.content.kinetics.crusher.CrushingWheelBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

public class ExtremeCrushingWheelBlockEntity extends CrushingWheelBlockEntity {
    public ExtremeCrushingWheelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static void handleCrushedMobDrops(LivingDropsEvent event) {
        DamageSource damageSource = event.getSource();
        if (damageSource == null || !damageSource.is(AllDamageTypes.CRUSH)) {
            return;
        }
        Vec3 outSpeed = Vec3.ZERO;
        for (ItemEntity outputItem : event.getDrops()) {
            outputItem.setDeltaMovement(outSpeed);
        }
    }
}
