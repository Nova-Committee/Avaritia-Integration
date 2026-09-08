package committee.nova.mods.avaritia_integration.integrations.create.content.extreme_crusher;

import com.simibubi.create.content.kinetics.crusher.CrushingWheelControllerBlock;
import com.simibubi.create.content.kinetics.crusher.CrushingWheelControllerBlockEntity;

import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationBlockEntityTypes;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class ExtremeCrushingWheelControllerBlock extends CrushingWheelControllerBlock {
    public ExtremeCrushingWheelControllerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends CrushingWheelControllerBlockEntity> getBlockEntityType() {
        return CreateIntegrationBlockEntityTypes.EXTREME_CRUSHING_WHEEL_CONTROLLER.get();
    }
}
