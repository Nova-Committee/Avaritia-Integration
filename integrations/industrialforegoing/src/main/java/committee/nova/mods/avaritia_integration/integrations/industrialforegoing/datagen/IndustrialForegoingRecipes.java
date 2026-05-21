package committee.nova.mods.avaritia_integration.integrations.industrialforegoing.datagen;

import committee.nova.mods.avaritia_integration.integrations.industrialforegoing.item.AddonItem;
import committee.nova.mods.avaritia_integration.integrations.industrialforegoing.registry.IndustrialForegoingIntegrationItems;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class IndustrialForegoingRecipes extends RecipeProvider {

    public IndustrialForegoingRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput consumer) {
        for (Supplier<? extends AddonItem> supplier : IndustrialForegoingIntegrationItems.ADDONS.values()) {
            supplier.get().registerRecipe(consumer);
        }
    }
}
