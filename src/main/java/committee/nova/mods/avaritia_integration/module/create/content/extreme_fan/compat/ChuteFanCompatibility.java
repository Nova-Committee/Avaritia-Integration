package committee.nova.mods.avaritia_integration.module.create.content.extreme_fan.compat;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public final class ChuteFanCompatibility {
    private ChuteFanCompatibility() {
    }

    public static boolean isFanFacing(Level level, BlockPos pos, Direction direction) {
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof ChuteInteractingFanBlock fanBlock)) {
            return false;
        }
        return fanBlock.getChuteFanFacing(state) == direction;
    }

    public static float getFanSpeed(Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof ChuteInteractingFanBlockEntity fan) || blockEntity.isRemoved()) {
            return 0;
        }
        return fan.getChuteFanSpeed();
    }
}
