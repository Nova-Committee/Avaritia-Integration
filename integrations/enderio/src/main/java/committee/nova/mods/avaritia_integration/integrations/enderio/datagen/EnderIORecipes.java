package committee.nova.mods.avaritia_integration.integrations.enderio.datagen;

import committee.nova.mods.avaritia.init.registry.ModItems;
import committee.nova.mods.avaritia_integration.integrations.enderio.registry.EnderIOIntegrationItems;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class EnderIORecipes extends RecipeProvider implements IConditionBuilder {

    public EnderIORecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EnderIOIntegrationItems.INFINITY_GRINDING_BALL.get())
                .pattern(" a ")
                .pattern("aaa")
                .pattern(" a ")
                .define('a', ModItems.infinity_ingot.get())
                .unlockedBy("has_item", has(ModItems.infinity_ingot.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EnderIOIntegrationItems.NEUTRON_GRINDING_BALL.get())
                .pattern(" a ")
                .pattern("aaa")
                .pattern(" a ")
                .define('a', ModItems.neutron_ingot.get())
                .unlockedBy("has_item", has(ModItems.neutron_ingot.get()))
                .save(consumer);
    }
}
