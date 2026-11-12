package committee.nova.mods.avaritia_integration.module.mekanismgenerators.common.tile;

import committee.nova.mods.avaritia_integration.module.mekanismgenerators.common.registry.GenIntegrationBlocks;
import mekanism.api.math.FloatingLong;
import mekanism.generators.common.tile.TileEntitySolarGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityInfinitySolarGenerator extends TileEntitySolarGenerator {

    public TileEntityInfinitySolarGenerator(BlockPos pos, BlockState state) {
        super(GenIntegrationBlocks.INFINITY_SOLAR_GENERATOR, pos, state, () -> FloatingLong.createConst(Long.MAX_VALUE));
    }

    @Override
    public FloatingLong getMaxOutput() {
        return super.getMaxOutput().min(FloatingLong.createConst(Long.MAX_VALUE));
    }

    @Override
    protected FloatingLong getConfiguredMax() {
        return FloatingLong.createConst(Long.MAX_VALUE);
    }
}
