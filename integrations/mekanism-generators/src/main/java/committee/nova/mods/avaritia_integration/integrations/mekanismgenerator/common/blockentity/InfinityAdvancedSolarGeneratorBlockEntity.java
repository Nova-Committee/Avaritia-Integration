package committee.nova.mods.avaritia_integration.integrations.mekanismgenerator.common.blockentity;

import committee.nova.mods.avaritia_integration.integrations.mekanismgenerator.common.registries.GenIntegrationBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class InfinityAdvancedSolarGeneratorBlockEntity extends AdvancedSolarGeneratorBlockEntity {

    public InfinityAdvancedSolarGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(GenIntegrationBlocks.INFINITY_ADVANCED_SOLAR_GENERATOR, pos, state, () -> Long.MAX_VALUE);
    }

    @Override
    protected long getConfiguredMax() {
        return Long.MAX_VALUE;
    }
}
