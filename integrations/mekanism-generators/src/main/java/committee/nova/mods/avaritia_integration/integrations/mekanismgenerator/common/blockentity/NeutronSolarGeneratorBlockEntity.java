package committee.nova.mods.avaritia_integration.integrations.mekanismgenerator.common.blockentity;

import committee.nova.mods.avaritia_integration.integrations.mekanism.common.config.MekIntegrationEnergy;
import committee.nova.mods.avaritia_integration.integrations.mekanismgenerator.common.registries.GenIntegrationBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import mekanism.generators.common.tile.TileEntitySolarGenerator;

public class NeutronSolarGeneratorBlockEntity extends TileEntitySolarGenerator {

    public NeutronSolarGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(GenIntegrationBlocks.NEUTRON_SOLAR_GENERATOR, pos, state,
                () -> MekIntegrationEnergy.NEUTRON_SOLAR_GENERATION);
    }

    @Override
    protected long getConfiguredMax() {
        return MekIntegrationEnergy.NEUTRON_SOLAR_GENERATION;
    }
}
