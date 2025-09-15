package committee.nova.mods.avaritia_integration.module.botania.item.block;

import committee.nova.mods.avaritia_integration.module.botania.item.block.entity.InfinityTinyPotatoBlockEntity;
import committee.nova.mods.avaritia_integration.module.botania.registry.BotaniaIntegrationBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.BotaniaWaterloggedBlock;
import vazkii.botania.common.block.block_entity.SimpleInventoryBlockEntity;
import vazkii.botania.common.block.block_entity.TinyPotatoBlockEntity;


public class InfinityTinyPotatoBlock extends BotaniaWaterloggedBlock implements EntityBlock {
    private static final VoxelShape SHAPE = box(4.0, 0.0, 4.0, 12.0, 12.0, 12.0);

    public InfinityTinyPotatoBlock() {
        super(Properties.copy(BotaniaBlocks.tinyPotato));
        this.registerDefaultState(this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        BlockEntity var5 = level.getBlockEntity(pos);
        if (var5 instanceof TinyPotatoBlockEntity tater) {
            return AbstractContainerMenu.getRedstoneSignalFromContainer(tater);
        } else {
            return 0;
        }
    }

    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof SimpleInventoryBlockEntity inventory) {
                Containers.dropContents(world, pos, inventory.getItemHandler());
                world.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, world, pos, newState, isMoving);
        }

    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        return SHAPE;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof InfinityTinyPotatoBlockEntity potatoTile) {
            ItemStack heldItem = player.getItemInHand(hand);
            potatoTile.interact(player, hand, heldItem, hit.getDirection());

            //粒子和音效
            double x = pos.getX();
            double y = pos.getY();
            double z = pos.getZ();
            RandomSource rand = world.random;
            for (int i = 0; i < 10; i++)
                world.addParticle(ParticleTypes.HEART, x + rand.nextDouble(), y + rand.nextDouble(), z + rand.nextDouble(), 0.0D, 0.1D + rand.nextDouble(), 0.0D);
//            if (heldItem.isEmpty() && player.isCrouching()) {
//                world.playSound(player, pos, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1.0f, 1.0f);
//                for (int i = 0; i < 5; i++)
//                    world.addParticle(ParticleTypes.EXPLOSION, x + rand.nextDouble(), y + rand.nextDouble(), z + rand.nextDouble(), 0.1D, 0.1D + rand.nextDouble(), 0.1D);
//            }
        }
        return InteractionResult.sidedSuccess(world.isClientSide());
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return super.getStateForPlacement(ctx).setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public @NotNull BlockState mirror(@NotNull BlockState state, Mirror mirror) {
        return state.setValue(BlockStateProperties.HORIZONTAL_FACING, mirror.mirror(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    @Override
    public @NotNull BlockState rotate(@NotNull BlockState state, Rotation rot) {
        return state.setValue(BlockStateProperties.HORIZONTAL_FACING, rot.rotate(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @org.jetbrains.annotations.Nullable LivingEntity living, ItemStack stack) {
        if (stack.hasCustomHoverName()) {
            BlockEntity var7 = world.getBlockEntity(pos);
            if (var7 instanceof TinyPotatoBlockEntity tater) {
                tater.name = stack.getHoverName();
            }
        }

    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new InfinityTinyPotatoBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, BotaniaIntegrationBlockEntities.INFINITY_TINY_POTATO.get(), InfinityTinyPotatoBlockEntity::commonTick);
    }
}