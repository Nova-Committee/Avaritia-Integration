package committee.nova.mods.avaritia_integration.integrations.create.content.extreme_crusher;

import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationBlockEntityTypes;
import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationBlocks;

import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import com.simibubi.create.content.kinetics.crusher.CrushingWheelBlockEntity;
import com.simibubi.create.content.kinetics.crusher.CrushingWheelControllerBlock;
import com.simibubi.create.content.kinetics.crusher.CrushingWheelControllerBlockEntity;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.infrastructure.config.AllConfigs;

public class ExtremeCrushingWheelControllerBlock extends CrushingWheelControllerBlock {

    public ExtremeCrushingWheelControllerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends CrushingWheelControllerBlockEntity> getBlockEntityType() {
        return CreateIntegrationBlockEntityTypes.EXTREME_CRUSHING_WHEEL_CONTROLLER.get();
    }

    @Override
    public void updateSpeed(BlockState state, LevelAccessor world, BlockPos pos) {
        withBlockEntityDo(world, pos, be -> {
            if (!state.getValue(VALID)) {
                if (be.crushingspeed != 0) {
                    be.crushingspeed = 0;
                    be.sendData();
                }
                return;
            }

            for (Direction d : Iterate.directions) {
                BlockState neighbour = world.getBlockState(pos.relative(d));
                if (!CreateIntegrationBlocks.EXTREME_CRUSHING_WHEEL.has(neighbour))
                    continue;
                if (neighbour.getValue(BlockStateProperties.AXIS) == d.getAxis())
                    continue;
                BlockEntity adjBE = world.getBlockEntity(pos.relative(d));
                if (!(adjBE instanceof CrushingWheelBlockEntity cwbe))
                    continue;
                be.crushingspeed = Math.abs(cwbe.getSpeed() / 50f);
                be.sendData();

                cwbe.award(AllAdvancements.CRUSHING_WHEEL);
                if (Math.abs(cwbe.getSpeed()) > AllConfigs.server().kinetics.maxRotationSpeed.get() - 1)
                    cwbe.award(AllAdvancements.CRUSHER_MAXED);

                break;
            }
        });
    }
}
