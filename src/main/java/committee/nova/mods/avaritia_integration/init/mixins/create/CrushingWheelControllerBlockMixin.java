package committee.nova.mods.avaritia_integration.init.mixins.create;

import com.simibubi.create.content.kinetics.crusher.CrushingWheelControllerBlock;
import committee.nova.mods.avaritia_integration.module.create.content.extreme_crusher.compat.BeltCrusherControllerBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CrushingWheelControllerBlock.class)
public class CrushingWheelControllerBlockMixin implements BeltCrusherControllerBlock {
    @Override
    public Direction getMovementFacing(BlockState state) {
        return state.getValue(CrushingWheelControllerBlock.FACING);
    }
}
