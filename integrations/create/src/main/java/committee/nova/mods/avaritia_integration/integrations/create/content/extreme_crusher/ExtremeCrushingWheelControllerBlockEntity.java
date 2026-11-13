package committee.nova.mods.avaritia_integration.integrations.create.content.extreme_crusher;

import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationBlockEntityTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import com.simibubi.create.content.kinetics.crusher.CrushingWheelControllerBlockEntity;

public class ExtremeCrushingWheelControllerBlockEntity extends CrushingWheelControllerBlockEntity {

    public ExtremeCrushingWheelControllerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void tick() {
        // Keep entity intake and damage on Create's normal path; only skip item processing time.
        if (crushingspeed != 0 && !hasEntity() && !inventory.isEmpty()) {
            inventory.remainingTime = 0;
        }
        super.tick();
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
                CreateIntegrationBlockEntityTypes.EXTREME_CRUSHING_WHEEL_CONTROLLER.get(),
                (be, context) -> be.inventory);
    }
}
