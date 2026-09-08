package committee.nova.mods.avaritia_integration.integrations.create.content;

import com.simibubi.create.api.boiler.BoilerHeater;
import committee.nova.mods.avaritia_integration.integrations.create.content.extreme_burner.ExtremeBlazeBurnerBlock;
import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BoilerHeaters {
    public static void registerDefaults() {
        BoilerHeater.REGISTRY.register(CreateIntegrationBlocks.EXTREME_BLAZE_BURNER.get(), BoilerHeaters::extremeBlazeBurner);
    }

    public static int extremeBlazeBurner(Level level, BlockPos pos, BlockState state) {
        ExtremeBlazeBurnerBlock.ExtremeHeatLevel value = state.getValue(ExtremeBlazeBurnerBlock.EXTREME_HEAT_LEVEL);
        if (value == ExtremeBlazeBurnerBlock.ExtremeHeatLevel.STAR) {
            return 3;
        }
        if (value.isAtLeast(ExtremeBlazeBurnerBlock.ExtremeHeatLevel.FADING)) {
            return 2;
        }
        return 1;
    }
}
