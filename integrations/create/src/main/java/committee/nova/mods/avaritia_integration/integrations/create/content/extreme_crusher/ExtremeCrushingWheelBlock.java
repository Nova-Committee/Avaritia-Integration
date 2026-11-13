package committee.nova.mods.avaritia_integration.integrations.create.content.extreme_crusher;

import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationBlockEntityTypes;
import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationBlocks;

import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import com.simibubi.create.content.kinetics.crusher.CrushingWheelBlock;
import com.simibubi.create.content.kinetics.crusher.CrushingWheelBlockEntity;

public class ExtremeCrushingWheelBlock extends CrushingWheelBlock {

    public ExtremeCrushingWheelBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends CrushingWheelBlockEntity> getBlockEntityType() {
        return CreateIntegrationBlockEntityTypes.EXTREME_CRUSHING_WHEEL.get();
    }

    @Override
    public void updateControllers(BlockState state, Level world, BlockPos pos, Direction side) {
        if (side.getAxis() == state.getValue(AXIS))
            return;
        if (world == null)
            return;

        BlockPos controllerPos = pos.relative(side);
        BlockPos otherWheelPos = pos.relative(side, 2);

        boolean controllerExists = CreateIntegrationBlocks.EXTREME_CRUSHING_WHEEL_CONTROLLER
                .has(world.getBlockState(controllerPos));
        boolean controllerIsValid = controllerExists && world.getBlockState(controllerPos)
                .getValue(ExtremeCrushingWheelControllerBlock.VALID);
        Direction controllerOldDirection = controllerExists ? world.getBlockState(controllerPos)
                .getValue(ExtremeCrushingWheelControllerBlock.FACING) : null;

        boolean controllerShouldExist = false;
        boolean controllerShouldBeValid = false;
        Direction controllerNewDirection = Direction.DOWN;

        BlockState otherState = world.getBlockState(otherWheelPos);
        if (CreateIntegrationBlocks.EXTREME_CRUSHING_WHEEL.has(otherState)) {
            controllerShouldExist = true;

            CrushingWheelBlockEntity be = getBlockEntity(world, pos);
            CrushingWheelBlockEntity otherBE = getBlockEntity(world, otherWheelPos);

            if (be != null && otherBE != null && (be.getSpeed() > 0) != (otherBE.getSpeed() > 0) &&
                    be.getSpeed() != 0 && otherBE.getSpeed() != 0) {
                Direction.Axis wheelAxis = state.getValue(AXIS);
                Direction.Axis sideAxis = side.getAxis();
                int controllerADO = Math.round(Math.signum(be.getSpeed())) * side.getAxisDirection()
                        .getStep();
                Vec3 controllerDirVec = new Vec3(wheelAxis == Direction.Axis.X ? 1 : 0,
                        wheelAxis == Direction.Axis.Y ? 1 : 0,
                        wheelAxis == Direction.Axis.Z ? 1 : 0).cross(
                                new Vec3(sideAxis == Direction.Axis.X ? 1 : 0, sideAxis == Direction.Axis.Y ? 1 : 0,
                                        sideAxis == Direction.Axis.Z ? 1 : 0));

                controllerNewDirection = Direction.getNearest(controllerDirVec.x * controllerADO,
                        controllerDirVec.y * controllerADO, controllerDirVec.z * controllerADO);

                controllerShouldBeValid = true;
            }
            if (otherState.getValue(AXIS) != state.getValue(AXIS))
                controllerShouldExist = false;
        }

        if (!controllerShouldExist) {
            if (controllerExists)
                world.setBlockAndUpdate(controllerPos, Blocks.AIR.defaultBlockState());
            return;
        }

        if (!controllerExists) {
            if (!world.getBlockState(controllerPos)
                    .canBeReplaced())
                return;
            world.setBlockAndUpdate(controllerPos,
                    CreateIntegrationBlocks.EXTREME_CRUSHING_WHEEL_CONTROLLER.getDefaultState()
                            .setValue(ExtremeCrushingWheelControllerBlock.VALID, controllerShouldBeValid)
                            .setValue(ExtremeCrushingWheelControllerBlock.FACING, controllerNewDirection));
        } else if (controllerIsValid != controllerShouldBeValid || controllerOldDirection != controllerNewDirection) {
            world.setBlockAndUpdate(controllerPos, world.getBlockState(controllerPos)
                    .setValue(ExtremeCrushingWheelControllerBlock.VALID, controllerShouldBeValid)
                    .setValue(ExtremeCrushingWheelControllerBlock.FACING, controllerNewDirection));
        }

        CreateIntegrationBlocks.EXTREME_CRUSHING_WHEEL_CONTROLLER.get()
                .updateSpeed(world.getBlockState(controllerPos), world, controllerPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        for (Direction direction : Iterate.directions) {
            BlockPos neighbourPos = pos.relative(direction);
            BlockState neighbourState = worldIn.getBlockState(neighbourPos);
            Direction.Axis stateAxis = state.getValue(AXIS);
            if (CreateIntegrationBlocks.EXTREME_CRUSHING_WHEEL_CONTROLLER.has(neighbourState) &&
                    direction.getAxis() != stateAxis)
                return false;
            if (!CreateIntegrationBlocks.EXTREME_CRUSHING_WHEEL.has(neighbourState))
                continue;
            if (neighbourState.getValue(AXIS) != stateAxis || stateAxis != direction.getAxis())
                return false;
        }

        return true;
    }
}
