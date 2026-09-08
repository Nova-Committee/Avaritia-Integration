package committee.nova.mods.avaritia_integration.integrations.create.content.extreme_depot;

import com.simibubi.create.content.logistics.depot.DepotBlockEntity;

import committee.nova.mods.avaritia_integration.integrations.create.mixin.DepotBlockEntityAccessor;
import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationBlockEntityTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class ExtremeDepotBlockEntity extends DepotBlockEntity {
    public ExtremeDepotBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
                CreateIntegrationBlockEntityTypes.EXTREME_DEPOT.get(),
                (be, context) -> ((DepotBlockEntityAccessor) be).avaritia$getDepotBehaviour().itemHandler);
    }
}
