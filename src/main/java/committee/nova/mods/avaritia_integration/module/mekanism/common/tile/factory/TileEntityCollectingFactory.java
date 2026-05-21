package committee.nova.mods.avaritia_integration.module.mekanism.common.tile.factory;

import committee.nova.mods.avaritia_integration.module.mekanism.api.recipes.cache.ChemicalToItemCachedRecipe;
import committee.nova.mods.avaritia_integration.module.mekanism.api.recipes.chemicals.ChemicalStackToItemStackRecipe;
import committee.nova.mods.avaritia_integration.module.mekanism.common.recipe.MekIntegrationRecipeType;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.math.MathUtils;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.lookup.ISingleRecipeLookupHandler.ChemicalRecipeLookupHandler;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.util.InventoryUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class TileEntityCollectingFactory extends TileEntityChemicalToItemMIFactory<ChemicalStackToItemStackRecipe> implements ChemicalRecipeLookupHandler<ChemicalStackToItemStackRecipe> {

    private static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            RecipeError.NOT_ENOUGH_INPUT,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT
    );
    private static final Set<RecipeError> GLOBAL_ERROR_TYPES = Set.of(RecipeError.NOT_ENOUGH_ENERGY);

    public TileEntityCollectingFactory(Holder<Block> blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES, GLOBAL_ERROR_TYPES);

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM);
    }

    @Override
    protected boolean isCachedRecipeValid(@Nullable CachedRecipe<ChemicalStackToItemStackRecipe> cached, @NotNull ChemicalStack stack) {
        return cached != null && cached.getRecipe().getInput().testType(stack);
    }

    @Override
    protected @Nullable ChemicalStackToItemStackRecipe findRecipe(int process, @NotNull ChemicalStack fallbackInput, @NotNull IInventorySlot outputSlots) {
        ItemStack output = outputSlots.getStack();
        return getRecipeType().getInputCache().findTypeBasedRecipe(level, fallbackInput, output, (recipe, input, currentOutput) -> InventoryUtils.areItemsStackable(recipe.getOutput(input), currentOutput));
    }

    @Override
    public boolean isChemicalValidForTank(@NotNull ChemicalStack stack) {
        return containsRecipe(stack);
    }

    @Override
    public boolean isValidInputChemical(@NotNull ChemicalStack stack) {
        return containsRecipe(stack);
    }

    @Override
    protected int getNeededInput(ChemicalStackToItemStackRecipe recipe, ChemicalStack inputStack) {
        return MathUtils.clampToInt(recipe.getInput().getNeededAmount(inputStack));
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<?, ChemicalStackToItemStackRecipe, InputRecipeCache.SingleChemical<ChemicalStackToItemStackRecipe>> getRecipeType() {
        return MekIntegrationRecipeType.COLLECTING;
    }

    @Override
    public @Nullable ChemicalStackToItemStackRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(gasInputHandlers[cacheIndex]);
    }

    @Override
    public @NotNull CachedRecipe<ChemicalStackToItemStackRecipe> createNewCachedRecipe(@NotNull ChemicalStackToItemStackRecipe recipe, int cacheIndex) {
        return ChemicalToItemCachedRecipe.chemicalToItem(recipe, recheckAllRecipeErrors[cacheIndex], gasInputHandlers[cacheIndex], itemOutputHandlers[cacheIndex])
                .setErrorsChanged(errors -> errorTracker.onErrorsChanged(errors, cacheIndex))
                .setCanHolderFunction(this::canFunction)
                .setActive(active -> setActiveState(active, cacheIndex))
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setRequiredTicks(this::getTicksRequired)
                .setOnFinish(this::markForSave)
                .setOperatingTicksChanged(operatingTicks -> progress[cacheIndex] = operatingTicks);
    }
}
