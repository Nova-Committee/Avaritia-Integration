package committee.nova.mods.avaritia_integration.module.mekanismgenerators.common.tile;

import committee.nova.mods.avaritia_integration.module.mekanismgenerators.common.registry.GenIntegrationBlocks;
import mekanism.api.math.FloatingLong;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityInfinityAdvancedSolarGenerator extends TileEntityAvaritiaAdvancedSolarGenerator {

    public TileEntityInfinityAdvancedSolarGenerator(BlockPos pos, BlockState state) {
        super(GenIntegrationBlocks.INFINITY_ADVANCED_SOLAR_GENERATOR, pos, state, () -> FloatingLong.createConst(Long.MAX_VALUE));
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
