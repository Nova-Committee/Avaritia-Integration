package committee.nova.mods.avaritia_integration.module.botania.item.block;

import committee.nova.mods.avaritia_integration.module.botania.item.block.entity.SoarleanderBlockEntity;
import committee.nova.mods.avaritia_integration.module.botania.registry.BotaniaIntegrationBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;
import vazkii.botania.common.block.BotaniaBlock;
import vazkii.botania.forge.block.ForgeSpecialFlowerBlock;

import java.util.function.Supplier;

public class SoarleanderBlock extends ForgeSpecialFlowerBlock {
    public SoarleanderBlock(MobEffect stewEffect, int stewDuration, BlockBehaviour.Properties props, Supplier<BlockEntityType<? extends SpecialFlowerBlockEntity>> blockEntityType) {
        super(stewEffect, stewDuration, props, blockEntityType);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new SoarleanderBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return BotaniaBlock.createTickerHelper(type, BotaniaIntegrationBlockEntities.SOARLEANDER_BLOCK_ENTITIES.get(), SoarleanderBlockEntity::commonTick);
    }
}
