package committee.nova.mods.avaritia_integration.module.mekanismgenerators.common.tile;

import committee.nova.mods.avaritia_integration.module.mekanismgenerators.common.registry.GenIntegrationBlocks;
import mekanism.api.math.FloatingLong;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityNeutronAdvancedSolarGenerator extends TileEntityAvaritiaAdvancedSolarGenerator {

    public TileEntityNeutronAdvancedSolarGenerator(BlockPos pos, BlockState state) {
        super(GenIntegrationBlocks.NEUTRON_ADVANCED_SOLAR_GENERATOR, pos, state, () -> FloatingLong.createConst(100_000_000_000L));
    }

    @Override
    protected FloatingLong getConfiguredMax() {
        return FloatingLong.createConst(100_000_000_000L);
    }
}
