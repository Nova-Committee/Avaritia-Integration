package committee.nova.mods.avaritia_integration.integrations.create.registry;

import com.simibubi.create.api.registry.SimpleRegistry;
import com.simibubi.create.api.stress.BlockStressValues;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import net.minecraft.world.level.block.Block;

import java.util.function.DoubleSupplier;

public class CreateIntegrationStress {
    public static final SimpleRegistry<Block, DoubleSupplier> IMPACTS = BlockStressValues.IMPACTS;
    public static final SimpleRegistry<Block, DoubleSupplier> CAPACITIES  = BlockStressValues.CAPACITIES ;

    public static <B extends Block> NonNullConsumer<B> setImpact(double value) {
        return block -> IMPACTS.register(block, () -> value);
    }

    public static <B extends Block> NonNullConsumer<B> setCapacity(double value) {
        return block -> CAPACITIES.register(block, () -> value);
    }
}
