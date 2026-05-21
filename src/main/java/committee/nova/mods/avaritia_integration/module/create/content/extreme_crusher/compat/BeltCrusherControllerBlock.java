package committee.nova.mods.avaritia_integration.module.create.content.extreme_crusher.compat;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public interface BeltCrusherControllerBlock {
    Direction getMovementFacing(BlockState state);
}
