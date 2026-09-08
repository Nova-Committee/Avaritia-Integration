package committee.nova.mods.avaritia_integration.integrations.create.compat.cca.liquid_burning;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import net.neoforged.neoforge.fluids.FluidStack;

public record FluidRecipeInput(FluidStack fluid) implements RecipeInput {

    @Override
    public ItemStack getItem(int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public int size() {
        return 1;
    }
}
