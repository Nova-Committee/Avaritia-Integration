package committee.nova.mods.avaritia_integration.init.mixins.create;

import com.simibubi.create.content.kinetics.belt.BeltHelper;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.kinetics.belt.transport.BeltCrusherInteractionHandler;
import com.simibubi.create.content.kinetics.belt.transport.BeltInventory;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import committee.nova.mods.avaritia_integration.init.mixins.create.accessor.BeltInventoryAccessor;
import committee.nova.mods.avaritia_integration.module.create.content.extreme_crusher.compat.BeltCrusherCompatibility;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemHandlerHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeltCrusherInteractionHandler.class)
public class BeltCrusherInteractionHandlerMixin {
    @Inject(method = "checkForCrushers", at = @At("HEAD"), cancellable = true, remap = false)
    private static void checkCompatibleCrusherControllers(BeltInventory beltInventory, TransportedItemStack currentItem,
                                                          float nextOffset, CallbackInfoReturnable<Boolean> cir) {
        BeltInventoryAccessor accessor = (BeltInventoryAccessor) beltInventory;
        BeltBlockEntity belt = accessor.getBelt();
        boolean beltMovementPositive = accessor.isBeltMovementPositive();
        int firstUpcomingSegment = (int) Math.floor(currentItem.beltPosition);
        int step = beltMovementPositive ? 1 : -1;
        firstUpcomingSegment = Mth.clamp(firstUpcomingSegment, 0, belt.beltLength - 1);

        for (int segment = firstUpcomingSegment; beltMovementPositive ? segment <= nextOffset
                : segment + 1 >= nextOffset; segment += step) {
            BlockPos crusherPos = BeltHelper.getPositionForOffset(belt, segment)
                    .above();
            Level world = belt.getLevel();
            BlockState crusherState = world.getBlockState(crusherPos);
            if (!BeltCrusherCompatibility.isController(crusherState)) {
                continue;
            }
            Direction crusherFacing = BeltCrusherCompatibility.getMovementFacing(crusherState);
            Direction movementFacing = belt.getMovementFacing();
            if (crusherFacing != movementFacing) {
                continue;
            }

            float crusherEntry = segment + .5f;
            crusherEntry += .399f * (beltMovementPositive ? -1 : 1);
            float postCrusherEntry = crusherEntry + .799f * (!beltMovementPositive ? -1 : 1);

            boolean hasCrossed = nextOffset > crusherEntry && nextOffset < postCrusherEntry && beltMovementPositive
                    || nextOffset < crusherEntry && nextOffset > postCrusherEntry && !beltMovementPositive;
            if (!hasCrossed) {
                cir.setReturnValue(false);
                return;
            }
            currentItem.beltPosition = crusherEntry;

            BlockEntity blockEntity = world.getBlockEntity(crusherPos);
            ItemStack toInsert = currentItem.stack.copy();
            ItemStack remainder = BeltCrusherCompatibility.insertFromBelt(blockEntity, toInsert, false);
            if (toInsert.equals(remainder, false)) {
                cir.setReturnValue(true);
                return;
            }

            int notFilled = currentItem.stack.getCount() - toInsert.getCount();
            if (!remainder.isEmpty()) {
                remainder.grow(notFilled);
            } else if (notFilled > 0) {
                remainder = ItemHandlerHelper.copyStackWithSize(currentItem.stack, notFilled);
            }

            currentItem.stack = remainder;
            belt.notifyUpdate();
            cir.setReturnValue(true);
            return;
        }

        cir.setReturnValue(false);
    }
}
