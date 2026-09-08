package committee.nova.mods.avaritia_integration.integrations.create.compat.cca;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import org.jetbrains.annotations.Nullable;

public interface ICCABurnerCompat {
    IFluidHandler getFluidInventory();

    default boolean tryUpdateLiquid(ItemStack stack, @Nullable Player player, boolean simulate) {
        return false;
    }

    default FluidStack getFuelFluid() {
        return FluidStack.EMPTY;
    }
}
