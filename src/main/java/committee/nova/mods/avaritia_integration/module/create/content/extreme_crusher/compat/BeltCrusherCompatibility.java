package committee.nova.mods.avaritia_integration.module.create.content.extreme_crusher.compat;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public final class BeltCrusherCompatibility {
    private BeltCrusherCompatibility() {
    }

    public static boolean isController(BlockState state) {
        return state.getBlock() instanceof BeltCrusherControllerBlock;
    }

    public static Direction getMovementFacing(BlockState state) {
        return ((BeltCrusherControllerBlock) state.getBlock()).getMovementFacing(state);
    }

    public static ItemStack insertFromBelt(BlockEntity blockEntity, ItemStack stack, boolean simulate) {
        if (!(blockEntity instanceof BeltCrusherControllerBlockEntity controller)) {
            return stack;
        }
        return controller.insertFromBelt(stack, simulate);
    }
}
