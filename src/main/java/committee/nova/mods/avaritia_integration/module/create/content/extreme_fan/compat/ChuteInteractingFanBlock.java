package committee.nova.mods.avaritia_integration.module.create.content.extreme_fan.compat;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public interface ChuteInteractingFanBlock {
    Direction getChuteFanFacing(BlockState state);
}
