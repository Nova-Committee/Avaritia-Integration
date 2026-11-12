package committee.nova.mods.avaritia_integration.integrations.ifeu.datagen;

import committee.nova.mods.avaritia_integration.integrations.ifeu.registry.IFEUIntegrationItems;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;

import com.hrznstudio.titanium.api.IRecipeProvider;

import java.util.concurrent.CompletableFuture;

public class IFEURecipes extends RecipeProvider {

    public IFEURecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput consumer) {
        ((IRecipeProvider) IFEUIntegrationItems.ENERGY_ADDON_INFINITY.get()).registerRecipe(consumer);
        RecipeOutput conversions = new RecipeOutput() {

            @Override
            public net.minecraft.advancements.Advancement.Builder advancement() {
                return consumer.advancement();
            }

            @Override
            public void accept(net.minecraft.resources.ResourceLocation id,
                               net.minecraft.world.item.crafting.Recipe<?> recipe,
                               net.minecraft.advancements.AdvancementHolder advancement,
                               net.neoforged.neoforge.common.conditions.ICondition... conditions) {
                consumer.accept(id,
                        new committee.nova.mods.avaritia_integration.integrations.ifeu.LegacyEnergyConversionRecipe(
                                (net.minecraft.world.item.crafting.ShapelessRecipe) recipe),
                        advancement, conditions);
            }
        };
        for (var legacy : java.util.List.of(IFEUIntegrationItems.ENERGY_ADDON_BLAZE_CUBE,
                IFEUIntegrationItems.ENERGY_ADDON_CRYSTAL_MATRIX, IFEUIntegrationItems.ENERGY_ADDON_NEUTRON)) {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IFEUIntegrationItems.ENERGY_ADDON_INFINITY.get())
                    .requires(legacy.get())
                    .unlockedBy("has_legacy_addon", has(legacy.get()))
                    .save(conversions, committee.nova.mods.avaritia_integration.AvaritiaIntegration.rl(
                            "ifeu/convert_" + legacy.getId().getPath()));
        }
    }
}
