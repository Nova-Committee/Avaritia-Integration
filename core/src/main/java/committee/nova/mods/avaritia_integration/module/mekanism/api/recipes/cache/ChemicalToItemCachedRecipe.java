package committee.nova.mods.avaritia_integration.module.mekanism.api.recipes.cache;

import committee.nova.mods.avaritia_integration.module.mekanism.api.recipes.chemicals.ChemicalStackToItemStackRecipe;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.cache.OneInputCachedRecipe;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ChemicalToItemCachedRecipe<INPUT, OUTPUT, RECIPE extends MekanismRecipe<?> & Predicate<INPUT>> extends OneInputCachedRecipe<INPUT, OUTPUT, RECIPE> {

    /**
     * @param recipe           Recipe.
     * @param recheckAllErrors Returns {@code true} if processing should be continued even if an error is hit in order to gather all the errors. It is recommended to not
     *                         do this every tick or if there is no one viewing recipes.
     * @param inputHandler     Input handler.
     * @param outputHandler    Output handler.
     * @param inputSupplier    Supplier of the recipe's input ingredient.
     * @param outputGetter     Gets the recipe's output when given the corresponding input.
     * @param inputEmptyCheck  Checks if the input is empty.
     * @param outputEmptyCheck Checks if the output is empty (indicating something went horribly wrong).
     */
    protected ChemicalToItemCachedRecipe(RECIPE recipe, BooleanSupplier recheckAllErrors, IInputHandler<INPUT> inputHandler, IOutputHandler<OUTPUT> outputHandler,
                                         Supplier<? extends InputIngredient<INPUT>> inputSupplier, Function<INPUT, OUTPUT> outputGetter, Predicate<INPUT> inputEmptyCheck,
                                         Predicate<OUTPUT> outputEmptyCheck) {
        super(recipe, recheckAllErrors, inputHandler, outputHandler, inputSupplier, outputGetter, inputEmptyCheck, outputEmptyCheck);
    }

    public static ChemicalToItemCachedRecipe<@NotNull ChemicalStack, @NotNull ItemStack, ChemicalStackToItemStackRecipe> chemicalToItem(ChemicalStackToItemStackRecipe recipe,
                                                                                                                                          BooleanSupplier recheckAllErrors, IInputHandler<@NotNull ChemicalStack> inputHandler, IOutputHandler<@NotNull ItemStack> outputHandler) {
        return new ChemicalToItemCachedRecipe<>(recipe, recheckAllErrors, inputHandler, outputHandler, recipe::getInput, recipe::getOutput, ChemicalStack::isEmpty,
                ItemStack::isEmpty);
    }
}
