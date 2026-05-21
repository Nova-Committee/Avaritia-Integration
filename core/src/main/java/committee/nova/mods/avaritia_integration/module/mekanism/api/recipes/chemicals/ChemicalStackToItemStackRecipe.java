package committee.nova.mods.avaritia_integration.module.mekanism.api.recipes.chemicals;

import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.vanilla_input.SingleChemicalRecipeInput;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Contract;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@NothingNullByDefault
public abstract class ChemicalStackToItemStackRecipe extends MekanismRecipe<SingleChemicalRecipeInput> implements Predicate<ChemicalStack> {

    private final ChemicalStackIngredient input;
    private final ItemStack output;

    public ChemicalStackToItemStackRecipe(ChemicalStackIngredient input, ItemStack output) {
        this.input = Objects.requireNonNull(input, "Input cannot be null.");
        Objects.requireNonNull(output, "Output cannot be null.");
        if (output.isEmpty()) {
            throw new IllegalArgumentException("Output cannot be empty.");
        }
        this.output = output.copy();
    }

    @Override
    public boolean test(ChemicalStack input) {
        return this.input.test(input);
    }

    @Override
    public boolean matches(SingleChemicalRecipeInput input, Level level) {
        return !isIncomplete() && test(input.chemical());
    }

    @Override
    public ItemStack assemble(SingleChemicalRecipeInput input, HolderLookup.Provider provider) {
        return matches(input, null) ? getOutput(input.chemical()) : ItemStack.EMPTY;
    }

    public ChemicalStackIngredient getInput() {
        return input;
    }

    @Contract(value = "_ -> new", pure = true)
    public ItemStack getOutput(ChemicalStack input) {
        return output.copy();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return output.copy();
    }

    public List<ItemStack> getOutputDefinition() {
        return Collections.singletonList(output);
    }

    public ItemStack getOutputRaw() {
        return output;
    }

    @Override
    public boolean isIncomplete() {
        return input.hasNoMatchingInstances();
    }

    @Override
    public void logMissingTags() {
        input.logMissingTags();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChemicalStackToItemStackRecipe other = (ChemicalStackToItemStackRecipe) o;
        return input.equals(other.input) && ItemStack.matches(output, other.output);
    }

    @Override
    public int hashCode() {
        int hash = input.hashCode();
        hash = 31 * hash + ItemStack.hashItemAndComponents(output);
        hash = 31 * hash + output.getCount();
        return hash;
    }
}
