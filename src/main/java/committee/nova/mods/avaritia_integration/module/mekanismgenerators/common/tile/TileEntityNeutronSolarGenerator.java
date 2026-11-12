package committee.nova.mods.avaritia_integration.module.mekanismgenerators.common.tile;

import committee.nova.mods.avaritia_integration.module.mekanismgenerators.common.registry.GenIntegrationBlocks;
import mekanism.api.math.FloatingLong;
import mekanism.generators.common.tile.TileEntitySolarGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityNeutronSolarGenerator extends TileEntitySolarGenerator {

    public TileEntityNeutronSolarGenerator(BlockPos pos, BlockState state) {
        super(GenIntegrationBlocks.NEUTRON_SOLAR_GENERATOR, pos, state, () -> FloatingLong.createConst(10_000_000_000L));
    }

    @Override
    protected FloatingLong getConfiguredMax() {
        return FloatingLong.createConst(10_000_000_000L);
    }
}
