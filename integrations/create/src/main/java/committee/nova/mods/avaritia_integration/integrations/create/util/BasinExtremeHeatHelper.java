package committee.nova.mods.avaritia_integration.integrations.create.util;

import committee.nova.mods.avaritia_integration.integrations.create.content.extreme_basin.ExtremeBasinBlockEntity;
import committee.nova.mods.avaritia_integration.integrations.create.content.extreme_burner.ExtremeBlazeBurnerBlock;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BasinExtremeHeatHelper {
    @Nullable
    public static<K extends BlockEntity> ExtremeBlazeBurnerBlock.ExtremeHeatLevel getHeat(K basin) {
        Level level = basin.getLevel();
        if (level == null) return null;

        BlockState stateBelow = level.getBlockState(basin.getBlockPos().below(1));

        return ExtremeBasinBlockEntity.getExtremeHeatLevelOf(stateBelow);
    }
}
