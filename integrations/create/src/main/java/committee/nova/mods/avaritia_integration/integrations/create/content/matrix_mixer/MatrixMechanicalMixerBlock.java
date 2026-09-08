package committee.nova.mods.avaritia_integration.integrations.create.content.matrix_mixer;

import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlock;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;

import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationBlockEntityTypes;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class MatrixMechanicalMixerBlock extends MechanicalMixerBlock {
    public MatrixMechanicalMixerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends MechanicalMixerBlockEntity> getBlockEntityType() {
        return CreateIntegrationBlockEntityTypes.MATRIX_MECHANICAL_MIXER.get();
    }
}
