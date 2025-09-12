package committee.nova.mods.avaritia_integration.common.block;

import committee.nova.mods.avaritia_integration.common.blockentity.InfinityTinyPotatoBlockEntity;
import committee.nova.mods.avaritia_integration.init.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.level.block.state.properties.Property;
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
        this.registerDefaultState((BlockState)this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(new Property[]{BlockStateProperties.HORIZONTAL_FACING});
    }

    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        BlockEntity var5 = level.getBlockEntity(pos);
        if (var5 instanceof TinyPotatoBlockEntity tater) {
            return AbstractContainerMenu.getRedstoneSignalFromContainer(tater);
        } else {
            return 0;
        }
    }

    public void onRemove(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof SimpleInventoryBlockEntity) {
                SimpleInventoryBlockEntity inventory = (SimpleInventoryBlockEntity)be;
                Containers.dropContents(world, pos, inventory.getItemHandler());
                world.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, world, pos, newState, isMoving);
        }

    }

    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        return SHAPE;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof InfinityTinyPotatoBlockEntity potatoTile){
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

    public @NotNull BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return (BlockState)super.getStateForPlacement(ctx).setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getHorizontalDirection().getOpposite());
    }

    public @NotNull BlockState mirror(@NotNull BlockState state, Mirror mirror) {
        return (BlockState)state.setValue(BlockStateProperties.HORIZONTAL_FACING, mirror.mirror((Direction)state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    public @NotNull BlockState rotate(@NotNull BlockState state, Rotation rot) {
        return (BlockState)state.setValue(BlockStateProperties.HORIZONTAL_FACING, rot.rotate((Direction)state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @org.jetbrains.annotations.Nullable LivingEntity living, ItemStack stack) {
        if (stack.hasCustomHoverName()) {
            BlockEntity var7 = world.getBlockEntity(pos);
            if (var7 instanceof TinyPotatoBlockEntity) {
                TinyPotatoBlockEntity tater = (TinyPotatoBlockEntity)var7;
                tater.name = stack.getHoverName();
            }
        }

    }

    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new InfinityTinyPotatoBlockEntity(pos, state);
    }

    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.INFINITY_TINY_POTATO.get(), InfinityTinyPotatoBlockEntity::commonTick);
    }}