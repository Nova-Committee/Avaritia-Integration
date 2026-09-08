package committee.nova.mods.avaritia_integration.integrations.create.content.extreme_depot;

import com.simibubi.create.content.logistics.depot.DepotBlock;
import com.simibubi.create.content.logistics.depot.DepotBlockEntity;

import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationBlockEntityTypes;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class ExtremeDepotBlock extends DepotBlock {
    public ExtremeDepotBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends DepotBlockEntity> getBlockEntityType() {
        return CreateIntegrationBlockEntityTypes.EXTREME_DEPOT.get();
    }
}
