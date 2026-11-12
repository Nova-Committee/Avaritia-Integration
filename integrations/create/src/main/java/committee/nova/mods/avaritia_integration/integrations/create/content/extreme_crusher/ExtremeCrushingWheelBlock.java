package committee.nova.mods.avaritia_integration.integrations.create.content.extreme_crusher;

import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationBlockEntityTypes;

import net.minecraft.world.level.block.entity.BlockEntityType;

import com.simibubi.create.content.kinetics.crusher.CrushingWheelBlock;
import com.simibubi.create.content.kinetics.crusher.CrushingWheelBlockEntity;

public class ExtremeCrushingWheelBlock extends CrushingWheelBlock {

    public ExtremeCrushingWheelBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends CrushingWheelBlockEntity> getBlockEntityType() {
        return CreateIntegrationBlockEntityTypes.EXTREME_CRUSHING_WHEEL.get();
    }
}
