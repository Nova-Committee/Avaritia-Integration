package committee.nova.mods.avaritia_integration.common.block;

import committee.nova.mods.avaritia_integration.common.blockentity.AsgardDandelionBlockEntity;
import committee.nova.mods.avaritia_integration.init.registry.BotaniaReg;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;
import vazkii.botania.common.block.BotaniaBlock;
import vazkii.botania.forge.block.ForgeSpecialFlowerBlock;

import java.util.function.Supplier;

public class AsgardDandelionBlock extends ForgeSpecialFlowerBlock {
    public AsgardDandelionBlock(MobEffect stewEffect, int stewDuration, Properties props, Supplier<BlockEntityType<? extends SpecialFlowerBlockEntity>> blockEntityType) {
        super(stewEffect, stewDuration, props, blockEntityType);
    }

    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new AsgardDandelionBlockEntity(pos, state);
    }

    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return BotaniaBlock.createTickerHelper(type, BotaniaReg.ASGARD_DANDELION.get(), AsgardDandelionBlockEntity::commonTick);
    }
}