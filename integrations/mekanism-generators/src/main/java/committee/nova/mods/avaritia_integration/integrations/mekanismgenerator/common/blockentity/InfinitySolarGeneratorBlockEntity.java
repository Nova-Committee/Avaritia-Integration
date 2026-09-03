package committee.nova.mods.avaritia_integration.integrations.mekanismgenerator.common.blockentity;

import committee.nova.mods.avaritia_integration.integrations.mekanismgenerator.common.registries.GenIntegrationBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import mekanism.generators.common.tile.TileEntitySolarGenerator;

public class InfinitySolarGeneratorBlockEntity extends TileEntitySolarGenerator {

    public InfinitySolarGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(GenIntegrationBlocks.INFINITY_SOLAR_GENERATOR, pos, state, () -> Long.MAX_VALUE);
    }

    @Override
    protected long getConfiguredMax() {
        return Long.MAX_VALUE;
    }
}
