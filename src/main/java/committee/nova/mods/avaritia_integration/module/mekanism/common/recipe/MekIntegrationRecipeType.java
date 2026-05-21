package committee.nova.mods.avaritia_integration.module.mekanism.common.recipe;

import committee.nova.mods.avaritia_integration.module.mekanism.api.recipes.chemicals.ChemicalStackToItemStackRecipe;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.api.recipes.vanilla_input.SingleChemicalRecipeInput;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleChemical;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleItem;
import mekanism.common.registration.impl.RecipeTypeRegistryObject;
import net.minecraft.world.item.crafting.SingleRecipeInput;

public class MekIntegrationRecipeType {

    private MekIntegrationRecipeType() {
    }

    public static RecipeTypeRegistryObject<SingleChemicalRecipeInput, ChemicalStackToItemStackRecipe, SingleChemical<ChemicalStackToItemStackRecipe>> COLLECTING;

    public static RecipeTypeRegistryObject<SingleRecipeInput, ItemStackToItemStackRecipe, SingleItem<ItemStackToItemStackRecipe>> MEK_COMPRESSING;
}
