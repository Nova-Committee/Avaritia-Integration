package committee.nova.mods.avaritia_integration.integrations.create.content.extreme_fan;

import com.simibubi.create.content.kinetics.fan.EncasedFanBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ExtremeEncasedFanBlockEntity extends EncasedFanBlockEntity {
    public ExtremeEncasedFanBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        airCurrent = new ExtremeAirCurrent(this);
    }
}
