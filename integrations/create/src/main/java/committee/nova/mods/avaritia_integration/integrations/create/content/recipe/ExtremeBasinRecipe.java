package committee.nova.mods.avaritia_integration.integrations.create.content.recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.recipe.DummyCraftingContainer;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import committee.nova.mods.avaritia_integration.integrations.create.content.extreme_basin.ExtremeBasinBlockEntity;
import committee.nova.mods.avaritia_integration.integrations.create.content.extreme_burner.ExtremeBlazeBurnerBlock;
import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationRecipeTypes;
import committee.nova.mods.avaritia_integration.integrations.create.util.BasinExtremeHeatHelper;

import net.createmod.catnip.data.Iterate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.items.IItemHandler;

public class ExtremeBasinRecipe extends ExtremeProcessingRecipe {
    public static <K extends SmartBlockEntity> boolean match(K be, Recipe<?> recipe) {
        FilteringBehaviour filter;
        if (be instanceof BasinBlockEntity basin) {
            filter = basin.getFilter();
        } else if (be instanceof ExtremeBasinBlockEntity extremeBasin) {
            filter = extremeBasin.getFilter();
        } else {
            return false;
        }
        if (filter == null) {
            return false;
        }
        boolean filterTest = filter.test(recipe.getResultItem(be.getLevel().registryAccess()));
        if (recipe instanceof ExtremeBasinRecipe basinRecipe
                && basinRecipe.getRollableResults().isEmpty()
                && !basinRecipe.getFluidResults().isEmpty()) {
            filterTest = filter.test(basinRecipe.getFluidResults().getFirst());
        }
        return filterTest && apply(be, recipe, true);
    }

    public static <K extends SmartBlockEntity> boolean apply(K basin, Recipe<?> recipe) {
        return apply(basin, recipe, false);
    }

    private static <K extends SmartBlockEntity> boolean apply(K basin, Recipe<?> recipe, boolean test) {
        boolean isBasinRecipe = recipe instanceof ExtremeBasinRecipe;
        IItemHandler availableItems = basin.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, basin.getBlockPos(), null);
        IFluidHandler availableFluids = basin.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, basin.getBlockPos(), null);
        if (availableItems == null || availableFluids == null) {
            return false;
        }
        ExtremeBlazeBurnerBlock.ExtremeHeatLevel heat = BasinExtremeHeatHelper.getHeat(basin);
        if (heat == null) {
            return false;
        }
        if (isBasinRecipe && !((ExtremeBasinRecipe) recipe).getRequiredHeat().testBlazeBurner(heat)) {
            return false;
        }

        List<ItemStack> recipeOutputItems = new ArrayList<>();
        List<FluidStack> recipeOutputFluids = new ArrayList<>();
        List<Ingredient> ingredients = new LinkedList<>(recipe.getIngredients());
        List<SizedFluidIngredient> fluidIngredients = isBasinRecipe
                ? ((ExtremeBasinRecipe) recipe).getFluidIngredients()
                : Collections.emptyList();

        for (boolean simulate : Iterate.trueAndFalse) {
            if (!simulate && test) {
                return true;
            }
            int[] extractedItemsFromSlot = new int[availableItems.getSlots()];
            int[] extractedFluidsFromTank = new int[availableFluids.getTanks()];

            Ingredients:
            for (Ingredient ingredient : ingredients) {
                for (int slot = 0; slot < availableItems.getSlots(); slot++) {
                    if (simulate && availableItems.getStackInSlot(slot).getCount() <= extractedItemsFromSlot[slot]) {
                        continue;
                    }
                    ItemStack extracted = availableItems.extractItem(slot, 1, true);
                    if (!ingredient.test(extracted)) {
                        continue;
                    }
                    if (!simulate) {
                        availableItems.extractItem(slot, 1, false);
                    }
                    extractedItemsFromSlot[slot]++;
                    continue Ingredients;
                }
                return false;
            }

            boolean fluidsAffected = false;
            FluidIngredients:
            for (SizedFluidIngredient fluidIngredient : fluidIngredients) {
                int amountRequired = fluidIngredient.amount();
                for (int tank = 0; tank < availableFluids.getTanks(); tank++) {
                    FluidStack fluidStack = availableFluids.getFluidInTank(tank);
                    if (simulate && fluidStack.getAmount() <= extractedFluidsFromTank[tank]) {
                        continue;
                    }
                    if (!fluidIngredient.test(fluidStack)) {
                        continue;
                    }
                    int drainedAmount = Math.min(amountRequired, fluidStack.getAmount());
                    if (!simulate) {
                        fluidStack.shrink(drainedAmount);
                        fluidsAffected = true;
                    }
                    amountRequired -= drainedAmount;
                    if (amountRequired != 0) {
                        continue;
                    }
                    extractedFluidsFromTank[tank] += drainedAmount;
                    continue FluidIngredients;
                }
                return false;
            }

            if (fluidsAffected) {
                basin.getBehaviour(SmartFluidTankBehaviour.INPUT).forEach(SmartFluidTankBehaviour.TankSegment::onFluidStackChanged);
                basin.getBehaviour(SmartFluidTankBehaviour.OUTPUT).forEach(SmartFluidTankBehaviour.TankSegment::onFluidStackChanged);
            }

            if (simulate) {
                CraftingInput remainderInput = new DummyCraftingContainer(availableItems, extractedItemsFromSlot).asCraftInput();
                if (recipe instanceof ExtremeBasinRecipe basinRecipe) {
                    recipeOutputItems.addAll(basinRecipe.rollResults(basin.getLevel().random));
                    for (FluidStack fluidStack : basinRecipe.getFluidResults()) {
                        if (!fluidStack.isEmpty()) {
                            recipeOutputFluids.add(fluidStack);
                        }
                    }
                    for (ItemStack stack : basinRecipe.getRemainingItems(remainderInput)) {
                        if (!stack.isEmpty()) {
                            recipeOutputItems.add(stack);
                        }
                    }
                } else {
                    recipeOutputItems.add(recipe.getResultItem(basin.getLevel().registryAccess()));
                    if (recipe instanceof CraftingRecipe craftingRecipe) {
                        for (ItemStack stack : craftingRecipe.getRemainingItems(remainderInput)) {
                            if (!stack.isEmpty()) {
                                recipeOutputItems.add(stack);
                            }
                        }
                    }
                }
            }

            boolean accepted = false;
            if (basin instanceof ExtremeBasinBlockEntity basinBE) {
                accepted = basinBE.acceptOutputs(recipeOutputItems, recipeOutputFluids, simulate);
            } else if (basin instanceof BasinBlockEntity basinBE) {
                accepted = basinBE.acceptOutputs(recipeOutputItems, recipeOutputFluids, simulate);
            }
            if (!accepted) {
                return false;
            }
        }
        return true;
    }

    protected ExtremeBasinRecipe(IRecipeTypeInfo type, ExtremeProcessingRecipeParams params) {
        super(type, params);
    }

    public ExtremeBasinRecipe(ExtremeProcessingRecipeParams params) {
        this(CreateIntegrationRecipeTypes.EXTREME_BASIN, params);
    }

    @Override
    protected int getMaxInputCount() {
        return 81;
    }

    @Override
    protected int getMaxOutputCount() {
        return 4;
    }

    @Override
    protected int getMaxFluidInputCount() {
        return 2;
    }

    @Override
    protected int getMaxFluidOutputCount() {
        return 2;
    }

    @Override
    protected boolean canRequireHeat() {
        return true;
    }

    @Override
    protected boolean canSpecifyDuration() {
        return true;
    }

    @Override
    public boolean matches(RecipeInput input, @NotNull Level worldIn) {
        return false;
    }
}
