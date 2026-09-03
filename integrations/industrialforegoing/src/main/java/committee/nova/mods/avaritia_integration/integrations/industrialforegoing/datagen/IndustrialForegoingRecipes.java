package committee.nova.mods.avaritia_integration.integrations.industrialforegoing.datagen;

import committee.nova.mods.avaritia_integration.integrations.industrialforegoing.item.AddonItem;
import committee.nova.mods.avaritia_integration.integrations.industrialforegoing.registry.IndustrialForegoingIntegrationFluids;
import committee.nova.mods.avaritia_integration.integrations.industrialforegoing.registry.IndustrialForegoingIntegrationItems;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import com.buuz135.industrial.recipe.LaserDrillFluidRecipe;
import com.buuz135.industrial.recipe.LaserDrillRarity;
import com.buuz135.industrial.recipe.data.EntityData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class IndustrialForegoingRecipes extends RecipeProvider {

    public IndustrialForegoingRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput consumer) {
        for (Supplier<? extends AddonItem> supplier : IndustrialForegoingIntegrationItems.ADDONS.values()) {
            supplier.get().registerRecipe(consumer);
        }

        var elderlyMedullaRecipe = new LaserDrillFluidRecipe(
                SizedFluidIngredient.of(IndustrialForegoingIntegrationFluids.ELDERLY_MEDULLA.getSourceFluid().get(),
                        50),
                7, Optional.of(EntityData.of(EntityType.ELDER_GUARDIAN)),
                new LaserDrillRarity(new LaserDrillRarity.BiomeRarity(new ArrayList<>(), new ArrayList<>()),
                        new LaserDrillRarity.DimensionRarity(new ArrayList<>(), new ArrayList<>()), -64, 256, 8));
        var voidMatterRecipe = new LaserDrillFluidRecipe(
                SizedFluidIngredient.of(IndustrialForegoingIntegrationFluids.VOID_MATTER.getSourceFluid().get(), 20),
                15, Optional.empty(),
                new LaserDrillRarity(
                        new LaserDrillRarity.BiomeRarity(LaserDrillRarity.BiomeRarity.END, new ArrayList<>()),
                        new LaserDrillRarity.DimensionRarity(new ArrayList<>(), new ArrayList<>()), -32, 64, 8));
        LaserDrillFluidRecipe.createRecipe(consumer, "elderly_medulla", "industrialforegoing", elderlyMedullaRecipe);
        LaserDrillFluidRecipe.createRecipe(consumer, "void_matter", "industrialforegoing", voidMatterRecipe);
    }
}
