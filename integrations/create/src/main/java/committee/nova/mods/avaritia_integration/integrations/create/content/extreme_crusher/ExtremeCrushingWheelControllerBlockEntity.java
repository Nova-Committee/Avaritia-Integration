package committee.nova.mods.avaritia_integration.integrations.create.content.extreme_crusher;

import com.simibubi.create.content.kinetics.crusher.CrushingWheelControllerBlockEntity;

import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationBlockEntityTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class ExtremeCrushingWheelControllerBlockEntity extends CrushingWheelControllerBlockEntity {
    public ExtremeCrushingWheelControllerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
                CreateIntegrationBlockEntityTypes.EXTREME_CRUSHING_WHEEL_CONTROLLER.get(),
                (be, context) -> be.inventory);
    }
}
