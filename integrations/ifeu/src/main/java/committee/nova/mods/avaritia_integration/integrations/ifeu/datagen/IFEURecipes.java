package committee.nova.mods.avaritia_integration.integrations.ifeu.datagen;

import committee.nova.mods.avaritia_integration.integrations.ifeu.rregistry.IFEUIntegrationItems;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

import com.hrznstudio.titanium.api.IRecipeProvider;

import java.util.concurrent.CompletableFuture;

public class IFEURecipes extends RecipeProvider {

    public IFEURecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput consumer) {
        ((IRecipeProvider) IFEUIntegrationItems.ENERGY_ADDON_BLAZE_CUBE.get()).registerRecipe(consumer);
        ((IRecipeProvider) IFEUIntegrationItems.ENERGY_ADDON_CRYSTAL_MATRIX.get()).registerRecipe(consumer);
        ((IRecipeProvider) IFEUIntegrationItems.ENERGY_ADDON_NEUTRON.get()).registerRecipe(consumer);
        ((IRecipeProvider) IFEUIntegrationItems.ENERGY_ADDON_INFINITY.get()).registerRecipe(consumer);
    }
}
