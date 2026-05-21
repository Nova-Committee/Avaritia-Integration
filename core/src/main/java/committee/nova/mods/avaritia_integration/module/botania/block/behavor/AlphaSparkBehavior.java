package committee.nova.mods.avaritia_integration.module.botania.block.behavor;

import committee.nova.mods.avaritia_integration.module.botania.item.AlphaSparkItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import org.jetbrains.annotations.NotNull;

/**
 * @author cnlimiter
 */
public class AlphaSparkBehavior extends OptionalDispenseItemBehavior {

    @NotNull
    @Override
    protected ItemStack execute(BlockSource source, @NotNull ItemStack stack) {
        Level world = source.level().getLevel();
        Direction facing = source.state().getValue(DispenserBlock.FACING);
        BlockPos pos = source.blockEntity().getBlockPos().relative(facing);

        setSuccess(AlphaSparkItem.attachSpark(world, pos, stack));

        return stack;
    }
}