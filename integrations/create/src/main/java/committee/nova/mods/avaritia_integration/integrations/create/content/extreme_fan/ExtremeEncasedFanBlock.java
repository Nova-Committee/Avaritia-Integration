package committee.nova.mods.avaritia_integration.integrations.create.content.extreme_fan;

import com.simibubi.create.content.kinetics.fan.EncasedFanBlock;
import com.simibubi.create.content.kinetics.fan.EncasedFanBlockEntity;

import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationBlockEntityTypes;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class ExtremeEncasedFanBlock extends EncasedFanBlock {
    public ExtremeEncasedFanBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends EncasedFanBlockEntity> getBlockEntityType() {
        return CreateIntegrationBlockEntityTypes.EXTREME_ENCASED_FAN.get();
    }
}
