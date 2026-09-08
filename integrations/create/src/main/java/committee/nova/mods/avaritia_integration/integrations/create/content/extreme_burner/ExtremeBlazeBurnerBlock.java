package committee.nova.mods.avaritia_integration.integrations.create.content.extreme_burner;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.logistics.stockTicker.StockTickerBlockEntity;
import com.simibubi.create.content.logistics.stockTicker.StockTickerInteractionHandler;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;

import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationBlockEntityTypes;
import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationBlocks;

import net.createmod.catnip.lang.Lang;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.util.FakePlayer;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ExtremeBlazeBurnerBlock extends HorizontalDirectionalBlock
        implements IBE<ExtremeBlazeBurnerBlockEntity>, IWrenchable {
    public static final EnumProperty<ExtremeHeatLevel> EXTREME_HEAT_LEVEL = EnumProperty.create("extreme_blaze",
            ExtremeHeatLevel.class);
    public static final MapCodec<ExtremeBlazeBurnerBlock> CODEC = simpleCodec(ExtremeBlazeBurnerBlock::new);

    public ExtremeBlazeBurnerBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(EXTREME_HEAT_LEVEL, ExtremeHeatLevel.SMOULDERING)
                .setValue(BlazeBurnerBlock.HEAT_LEVEL, BlazeBurnerBlock.HeatLevel.KINDLED));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(EXTREME_HEAT_LEVEL, BlazeBurnerBlock.HEAT_LEVEL, FACING);
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (world.isClientSide) {
            return;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos.above());
        if (blockEntity instanceof BasinBlockEntity basin) {
            basin.notifyChangeOfContents();
        }
    }

    @Override
    public Class<ExtremeBlazeBurnerBlockEntity> getBlockEntityClass() {
        return ExtremeBlazeBurnerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ExtremeBlazeBurnerBlockEntity> getBlockEntityType() {
        return CreateIntegrationBlockEntityTypes.EXTREME_HEATER.get();
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hit) {
        if (AllItems.GOGGLES.isIn(stack)) {
            return onBlockEntityUseItemOn(world, pos, ebe -> {
                if (ebe.goggles) {
                    return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                }
                ebe.goggles = true;
                ebe.notifyUpdate();
                return ItemInteractionResult.SUCCESS;
            });
        }

        ExtremeBlazeBurnerBlockEntity be = getBlockEntity(world, pos);
        if (be != null && be.stockKeeper) {
            StockTickerBlockEntity stockTicker = BlazeBurnerBlockEntity.getStockTicker(world, pos);
            if (stockTicker != null) {
                StockTickerInteractionHandler.interactWithLogisticsManagerAt(player, world, stockTicker.getBlockPos());
                return ItemInteractionResult.SUCCESS;
            }
        }

        if (stack.isEmpty()) {
            return onBlockEntityUseItemOn(world, pos, ebe -> {
                if (!ebe.goggles) {
                    return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                }
                ebe.goggles = false;
                ebe.notifyUpdate();
                return ItemInteractionResult.SUCCESS;
            });
        }

        boolean doNotConsume = player.isCreative();
        boolean forceOverflow = player instanceof FakePlayer == false;
        InteractionResultHolder<ItemStack> res = tryInsert(state, world, pos, stack, doNotConsume, forceOverflow,
                false);
        ItemStack leftover = res.getObject();
        if (!world.isClientSide && !doNotConsume && !leftover.isEmpty()) {
            if (stack.isEmpty()) {
                player.setItemInHand(hand, leftover);
            } else if (!player.getInventory().add(leftover)) {
                player.drop(leftover, false);
            }
        }
        return res.getResult() == InteractionResult.SUCCESS ? ItemInteractionResult.SUCCESS
                : ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public static InteractionResultHolder<ItemStack> tryInsert(BlockState state, Level world, BlockPos pos,
            ItemStack stack, boolean doNotConsume, boolean forceOverflow, boolean simulate) {
        if (!state.hasBlockEntity()) {
            return InteractionResultHolder.fail(ItemStack.EMPTY);
        }
        BlockEntity be = world.getBlockEntity(pos);
        if (!(be instanceof ExtremeBlazeBurnerBlockEntity burnerBE)) {
            return InteractionResultHolder.fail(ItemStack.EMPTY);
        }
        if (burnerBE.isCreativeFuel(stack)) {
            if (!simulate) {
                burnerBE.applyCreativeFuel();
            }
            return InteractionResultHolder.success(ItemStack.EMPTY);
        }
        if (!burnerBE.tryUpdateFuel(stack, forceOverflow, simulate)) {
            if (!burnerBE.tryUpdateLiquid(stack, null, simulate)) {
                return InteractionResultHolder.fail(ItemStack.EMPTY);
            }
        }
        if (!doNotConsume) {
            ItemStack container = stack.hasCraftingRemainingItem() ? stack.getCraftingRemainingItem() : ItemStack.EMPTY;
            if (!world.isClientSide) {
                stack.shrink(1);
            }
            return InteractionResultHolder.success(container);
        }
        return InteractionResultHolder.success(ItemStack.EMPTY);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        ItemStack stack = context.getItemInHand();
        Item item = stack.getItem();
        BlockState defaultState = defaultBlockState();
        if (!item.equals(CreateIntegrationBlocks.EXTREME_BLAZE_BURNER.asItem())) {
            return defaultState;
        }
        return defaultState.setValue(EXTREME_HEAT_LEVEL, ExtremeHeatLevel.SMOULDERING)
                .setValue(BlazeBurnerBlock.HEAT_LEVEL, BlazeBurnerBlock.HeatLevel.KINDLED)
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
        return AllShapes.HEATER_BLOCK_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
        if (context == CollisionContext.empty()) {
            return AllShapes.HEATER_BLOCK_SPECIAL_COLLISION_SHAPE;
        }
        return getShape(state, reader, pos, context);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return Math.max(0, state.getValue(EXTREME_HEAT_LEVEL).ordinal() - 1);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (random.nextInt(10) != 0) {
            return;
        }
        world.playLocalSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, SoundEvents.CAMPFIRE_CRACKLE,
                SoundSource.BLOCKS, 0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.6F, false);
    }

    public static BlazeBurnerBlock.HeatLevel getHeatLevelOf(BlockState blockState) {
        if (!blockState.hasProperty(EXTREME_HEAT_LEVEL)) {
            return BlazeBurnerBlock.HeatLevel.KINDLED;
        }
        return blockState.getValue(EXTREME_HEAT_LEVEL).isAtLeast(ExtremeHeatLevel.FADING)
                ? BlazeBurnerBlock.HeatLevel.SEETHING
                : BlazeBurnerBlock.HeatLevel.KINDLED;
    }

    public static ExtremeHeatLevel getExtremeHeatLevelOf(BlockState blockState) {
        return blockState.hasProperty(EXTREME_HEAT_LEVEL) ? blockState.getValue(EXTREME_HEAT_LEVEL)
                : ExtremeHeatLevel.SMOULDERING;
    }

    public static int getLight(BlockState state) {
        ExtremeHeatLevel level = state.getValue(EXTREME_HEAT_LEVEL);
        return level == ExtremeHeatLevel.SMOULDERING ? 8 : 15;
    }

    public static void blockStateDataGen(DataGenContext<Block, ExtremeBlazeBurnerBlock> ctx,
            RegistrateBlockstateProvider provider) {
        provider.getVariantBuilder(ctx.get()).forAllStates(state -> {
            ExtremeHeatLevel heatLevel = state.getValue(EXTREME_HEAT_LEVEL);
            Direction facing = state.getValue(FACING);
            if (heatLevel == ExtremeHeatLevel.SMOULDERING) {
                return ConfiguredModel.builder()
                        .modelFile(new ModelFile.UncheckedModelFile(
                                ResourceLocation.fromNamespaceAndPath("create", "block/blaze_burner/block")))
                        .rotationY(((int) facing.toYRot() + 180) % 360)
                        .build();
            }
            String fileName = heatLevel == ExtremeHeatLevel.STAR ? "block_star" : "block_blaze";
            return ConfiguredModel.builder()
                    .modelFile(provider.models()
                            .getExistingFile(provider.modLoc("block/create/extreme_blaze_burner/block/" + fileName)))
                    .rotationY(((int) facing.toYRot() + 180) % 360)
                    .build();
        });
    }

    public enum ExtremeHeatLevel implements StringRepresentable {
        SMOULDERING, FADING, BLAZE, STAR;

        public static ExtremeHeatLevel byIndex(int index) {
            return values()[index];
        }

        public boolean isAtLeast(ExtremeHeatLevel heatLevel) {
            return ordinal() >= heatLevel.ordinal();
        }

        @Override
        public String getSerializedName() {
            return Lang.asId(name());
        }
    }
}
