package committee.nova.mods.avaritia_integration.integrations.create.content.extreme_depot;

import com.simibubi.create.api.contraption.storage.item.MountedItemStorageType;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

public class ExtremeDepotMountedStorageType extends MountedItemStorageType<ExtremeDepotMountedStorage> {

    public ExtremeDepotMountedStorageType() {
        super(ExtremeDepotMountedStorage.CODEC);
    }

    @Override
    public @Nullable ExtremeDepotMountedStorage mount(Level level, BlockState state, BlockPos pos,
            @Nullable BlockEntity be) {
        if (be instanceof ExtremeDepotBlockEntity depot) {
            return ExtremeDepotMountedStorage.fromDepot(depot);
        }
        return null;
    }
}
