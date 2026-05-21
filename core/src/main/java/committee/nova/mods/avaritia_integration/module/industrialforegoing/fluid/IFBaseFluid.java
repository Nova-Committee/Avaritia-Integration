package committee.nova.mods.avaritia_integration.module.industrialforegoing.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class IFBaseFluid extends FlowingFluid {
    private final IFBaseFluidInstance baseFluidInstance;

    public IFBaseFluid(IFBaseFluidInstance baseFluidInstance) {
        this.baseFluidInstance = baseFluidInstance;
    }

    @Nonnull
    public Fluid getFlowing() {
        return this.baseFluidInstance.getFlowingFluid().get();
    }

    @Nonnull
    public Fluid getSource() {
        return this.baseFluidInstance.getSourceFluid().get();
    }

    public boolean canConvertToSource(@NotNull FluidState state, @NotNull Level level, @NotNull BlockPos pos) {
        return false;
    }

    protected boolean canConvertToSource(@NotNull Level level) {
        return false;
    }

    @ParametersAreNonnullByDefault
    protected void beforeDestroyingBlock(LevelAccessor worldIn, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = worldIn.getBlockEntity(pos);
        if (blockEntity != null) {
            Block.dropResources(state, worldIn, pos, blockEntity);
        }
    }

    protected int getSlopeFindDistance(@Nonnull LevelReader world) {
        return 4;
    }

    protected int getDropOff(@Nonnull LevelReader world) {
        return 1;
    }

    @Nonnull
    public Item getBucket() {
        return this.baseFluidInstance.getBucketFluid();
    }

    @ParametersAreNonnullByDefault
    protected boolean canBeReplacedWith(FluidState fluidState, BlockGetter level, BlockPos blockPos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluidState.is(FluidTags.WATER);
    }

    public int getTickDelay(@Nonnull LevelReader p_205569_1_) {
        return 5;
    }

    protected float getExplosionResistance() {
        return 1.0F;
    }

    @Nonnull
    protected BlockState createLegacyBlock(@Nonnull FluidState state) {
        return this.baseFluidInstance.getBlockFluid().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
    }

    public boolean isSource(@Nonnull FluidState state) {
        return false;
    }

    public int getAmount(@Nonnull FluidState state) {
        return 0;
    }

    public boolean isSame(@NotNull Fluid fluidIn) {
        return fluidIn == this.baseFluidInstance.getFlowingFluid().get() || fluidIn == this.baseFluidInstance.getSourceFluid().get();
    }

    public @NotNull FluidType getFluidType() {
        return this.baseFluidInstance.getFluidType().get();
    }
}