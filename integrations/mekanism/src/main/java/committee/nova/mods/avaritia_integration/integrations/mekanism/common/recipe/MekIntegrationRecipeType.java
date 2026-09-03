package committee.nova.mods.avaritia_integration.integrations.mekanism.common.recipe;

import committee.nova.mods.avaritia_integration.integrations.mekanism.api.recipes.chemicals.ChemicalStackToItemStackRecipe;

import net.minecraft.world.item.crafting.SingleRecipeInput;

import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.api.recipes.vanilla_input.SingleChemicalRecipeInput;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleChemical;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleItem;
import mekanism.common.registration.impl.RecipeTypeRegistryObject;

public class MekIntegrationRecipeType {

    private MekIntegrationRecipeType() {}

    public static RecipeTypeRegistryObject<SingleChemicalRecipeInput, ChemicalStackToItemStackRecipe, SingleChemical<ChemicalStackToItemStackRecipe>> COLLECTING;

    public static RecipeTypeRegistryObject<SingleRecipeInput, ItemStackToItemStackRecipe, SingleItem<ItemStackToItemStackRecipe>> MEK_COMPRESSING;
}
