package committee.nova.mods.avaritia_integration.integrations.ae2.datagen;

import committee.nova.mods.avaritia.init.data.provider.recipe.ModShapedRecipeBuilder;
import committee.nova.mods.avaritia.init.registry.ModItems;
import committee.nova.mods.avaritia_integration.integrations.ae2.registry.AE2IntegrationItems;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;

import java.util.concurrent.CompletableFuture;

public class AE2Recipes extends RecipeProvider implements IConditionBuilder {

    public AE2Recipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput consumer) {
        ModShapedRecipeBuilder.shaped(RecipeCategory.MISC, AE2IntegrationItems.INFINITY_ME_STORAGE_COMPONENT.get())
                .pattern("  rrrrr  ")
                .pattern(" vesssev ")
                .pattern("reafcfaer")
                .pattern("rsfxzxfsr")
                .pattern("rsczdzcsr")
                .pattern("rsfxzxfsr")
                .pattern("reafcfaer")
                .pattern(" vesssev ")
                .pattern("  rrrrr  ")
                .define('a', AEItems.ENDER_DUST)
                .define('s', AEItems.SKY_DUST)
                .define('d', AEBlocks.CONTROLLER)
                .define('f', AEItems.CELL_COMPONENT_256K)
                .define('z', AEItems.SPATIAL_128_CELL_COMPONENT)
                .define('x', AEItems.MATTER_BALL)
                .define('c', AEItems.SINGULARITY)
                .define('e', ModItems.infinity_catalyst.get())
                .define('r', ModItems.infinity_ingot.get())
                .define('v', ModItems.neutron_gear.get())
                .unlockedBy("has_item", has(ModItems.infinity_ingot.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AE2IntegrationItems.INFINITY_ME_STORAGE_CELL.get())
                .pattern("aq")
                .pattern("s ")
                .define('a', AE2IntegrationItems.INFINITY_ME_STORAGE_COMPONENT.get())
                .define('q', AEItems.FLUID_CELL_HOUSING)
                .define('s', AEItems.ITEM_CELL_HOUSING)
                .unlockedBy("has_item", has(ModItems.neutron_ingot.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AE2IntegrationItems.INFINITY_ME_STORAGE_CELL_BIG.get())
                .pattern("aq")
                .define('a', AE2IntegrationItems.INFINITY_ME_STORAGE_COMPONENT.get())
                .define('q', AE2IntegrationItems.INFINITY_ME_STORAGE_CELL.get())
                .unlockedBy("has_item", has(ModItems.neutron_ingot.get()))
                .save(consumer);
    }
}
