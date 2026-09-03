package committee.nova.mods.avaritia_integration.integrations.botania.botania.block;

import committee.nova.mods.avaritia_integration.integrations.botania.botania.entity.InfinityManaPoolBlockEntity;
import committee.nova.mods.avaritia_integration.integrations.botania.botania.registry.BotaniaIntegrationBlockEntities;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.internal.Colored;
import vazkii.botania.api.internal.OptionallyColored;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.BotaniaWaterloggedBlock;
import vazkii.botania.common.entity.ManaBurstEntity;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.List;
import java.util.Optional;

/**
 * 改自{@link vazkii.botania.common.block.mana.ManaPoolBlock}
 */
public class InfinityManaPoolBlock extends BotaniaWaterloggedBlock implements EntityBlock, OptionallyColored {

    public static final int MAX_MANA = Integer.MAX_VALUE;

    private static final VoxelShape NORMAL_SHAPE_INTERACT = box(0, 0, 0, 16, 8, 16);
    private static final VoxelShape NORMAL_SHAPE_CUTOUT = box(2, 2, 2, 14, 16, 14);
    private static final VoxelShape NORMAL_SHAPE = Shapes.join(NORMAL_SHAPE_INTERACT, NORMAL_SHAPE_CUTOUT,
            BooleanOp.ONLY_FIRST);

    public record ShapeVariant(VoxelShape interactionShape, VoxelShape cutoutShape, VoxelShape collisionShape) {}

    public static final ShapeVariant NORMAL_SHAPE_VARIANT = new ShapeVariant(NORMAL_SHAPE_INTERACT, NORMAL_SHAPE_CUTOUT,
            NORMAL_SHAPE);

    public final boolean creative = false;
    public final boolean fabulous = false;
    public final int manaCapacity = Integer.MAX_VALUE;
    @Nullable
    public final DyeColor color = null;
    public final ShapeVariant shapeVariant = NORMAL_SHAPE_VARIANT;

    public InfinityManaPoolBlock(Properties builder) {
        super(builder);
        registerDefaultState(defaultBlockState().setValue(BotaniaStateProperties.OUTPUTTING, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BotaniaStateProperties.OUTPUTTING);
    }

    public static InfinityManaPoolBlock getUndyedBlock(InfinityManaPoolBlock potentiallyDyedBlock) {
        return BotaniaBlocks.findOptionallyDyedBlock(potentiallyDyedBlock, null);
    }

    public boolean isCreative() {
        return creative;
    }

    public boolean isFabulous() {
        return fabulous;
    }

    public int getManaCapacity() {
        return manaCapacity;
    }

    @Override
    public String getDescriptionId() {
        if (creative) {
            return Util.makeDescriptionId("block",
                    BuiltInRegistries.BLOCK.getKey(getUndyedBlock(this)));
        }
        return super.getDescriptionId();
    }

    @Override
    public MutableComponent getName() {
        if (creative && color != null) {
            return Component.translatable("botaniamisc.template.parenthesis_suffix",
                    super.getName(), Component.translatable("color.minecraft." + color.getSerializedName()));
        }
        return super.getName();
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip,
                                TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        if (creative) {
            for (int i = 0; i < 2; i++) {
                tooltip.add(Component.translatable("botaniamisc.creativePool" + i).withStyle(ChatFormatting.GRAY));
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        return shapeVariant.collisionShape();
    }

    public VoxelShape getInnerShape(BlockState state) {
        return shapeVariant.cutoutShape();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        // Sometimes the pool's collision box is too thin for bursts shot straight up.
        return context instanceof EntityCollisionContext ecc && ecc.getEntity() instanceof ManaBurstEntity ?
                getInteractionShape(state, world, pos) : super.getCollisionShape(state, world, pos, context);
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, @Nullable BlockGetter level, @Nullable BlockPos pos) {
        return shapeVariant.interactionShape();
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(BotaniaTags.Items.PETALS) && stack.getItem() instanceof Colored colorSource) {
            DyeColor itemColor = colorSource.getColor();
            if (!itemColor.equals(this.color)) {
                InfinityManaPoolBlock dyedBlock = BotaniaBlocks.findOptionallyDyedBlock(this, itemColor);
                level.setBlockAndUpdate(pos, dyedBlock.withPropertiesOf(state));
                if (!player.hasInfiniteMaterials()) {
                    stack.shrink(1);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide());
            }
        }
        if (stack.is(BotaniaTags.Items.MANA_POOL_DYE_REMOVER) && this.color != null) {
            InfinityManaPoolBlock undyedBlock = getUndyedBlock(this);
            level.setBlockAndUpdate(pos, undyedBlock.withPropertiesOf(state));
            if (!player.hasInfiniteMaterials()) {
                stack.shrink(1);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() instanceof InfinityManaPoolBlock oldBlock &&
                newState.getBlock() instanceof InfinityManaPoolBlock newBlock &&
                InfinityManaPoolBlock.getUndyedBlock(oldBlock) == InfinityManaPoolBlock.getUndyedBlock(newBlock)) {
            // don't delete block entity if it's the same pool type
            return;
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new InfinityManaPoolBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                  BlockEntityType<T> type) {
        return createTickerHelper(type, BotaniaIntegrationBlockEntities.INFINITY_MANA_POOL.get(),
                level.isClientSide ? InfinityManaPoolBlockEntity::clientTick : InfinityManaPoolBlockEntity::serverTick);
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        if (entity instanceof ItemEntity item &&
                world.getBlockEntity(pos) instanceof InfinityManaPoolBlockEntity pool) {
            pool.collideEntityItem(item);
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        if (fabulous) {
            return RenderShape.ENTITYBLOCK_ANIMATED;
        } else {
            return RenderShape.MODEL;
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return world.getBlockEntity(pos) instanceof InfinityManaPoolBlockEntity pool ?
                InfinityManaPoolBlockEntity.calculateComparatorLevel(pool.getCurrentMana(), pool.getMaxMana()) : 0;
    }

    @Override
    public Optional<DyeColor> getOptionalColor() {
        return Optional.ofNullable(this.color);
    }
}
