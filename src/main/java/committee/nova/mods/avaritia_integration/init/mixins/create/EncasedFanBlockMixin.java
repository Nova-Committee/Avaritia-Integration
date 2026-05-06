package committee.nova.mods.avaritia_integration.init.mixins.create;

import com.simibubi.create.content.kinetics.fan.EncasedFanBlock;
import committee.nova.mods.avaritia_integration.module.create.content.extreme_fan.compat.ChuteInteractingFanBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EncasedFanBlock.class)
public class EncasedFanBlockMixin implements ChuteInteractingFanBlock {
    @Override
    public Direction getChuteFanFacing(BlockState state) {
        return state.getValue(EncasedFanBlock.FACING);
    }
}
