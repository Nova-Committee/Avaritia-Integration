package committee.nova.mods.avaritia_integration.integrations.mekanismgenerator.common.blockentity;

import committee.nova.mods.avaritia_integration.integrations.mekanism.common.config.MekIntegrationEnergy;
import committee.nova.mods.avaritia_integration.integrations.mekanismgenerator.common.registries.GenIntegrationBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class NeutronAdvancedSolarGeneratorBlockEntity extends AdvancedSolarGeneratorBlockEntity {

    public NeutronAdvancedSolarGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(GenIntegrationBlocks.NEUTRON_ADVANCED_SOLAR_GENERATOR, pos, state,
                () -> MekIntegrationEnergy.NEUTRON_ADVANCED_SOLAR_GENERATION);
    }

    @Override
    protected long getConfiguredMax() {
        return MekIntegrationEnergy.NEUTRON_ADVANCED_SOLAR_GENERATION;
    }
}
