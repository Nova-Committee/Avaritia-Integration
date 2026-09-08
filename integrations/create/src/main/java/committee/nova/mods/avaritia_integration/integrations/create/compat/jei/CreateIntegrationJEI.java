package committee.nova.mods.avaritia_integration.integrations.create.compat.jei;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.Create;
import com.simibubi.create.compat.jei.CreateJEI;
import com.simibubi.create.compat.jei.DoubleItemIcon;
import com.simibubi.create.compat.jei.EmptyBackground;
import com.simibubi.create.compat.jei.ItemIcon;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.kinetics.fan.processing.HauntingRecipe;
import com.simibubi.create.content.kinetics.fan.processing.SplashingRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.basin.BasinRecipe;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.integrations.create.compat.cca.liquid_burning.LiquidBurningRecipe;
import committee.nova.mods.avaritia_integration.integrations.create.compat.jei.category.ExtremeMixingCategory;
import committee.nova.mods.avaritia_integration.integrations.create.compat.jei.category.LiquidBurningCategory;
import committee.nova.mods.avaritia_integration.integrations.create.content.recipe.ExtremeBasinRecipe;
import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationBlocks;
import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationRecipeTypes;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.level.ItemLike;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;

import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@JeiPlugin
@ParametersAreNonnullByDefault
public class CreateIntegrationJEI implements IModPlugin {

    private static final ResourceLocation ID = AvaritiaIntegration.rl("create_jei");
    private final List<CreateRecipeCategory<?>> allCategories = new ArrayList<>();

    private void loadCategories() {
        allCategories.clear();
        allCategories.add(build("extreme_mixing", ExtremeBasinRecipe.class,
                () -> typed(CreateIntegrationRecipeTypes.EXTREME_MIXING.getType()),
                List.<Supplier<? extends ItemStack>>of(CreateIntegrationBlocks.MATRIX_MECHANICAL_MIXER::asStack,
                        CreateIntegrationBlocks.EXTREME_BASIN::asStack, AllBlocks.MECHANICAL_MIXER::asStack,
                        AllBlocks.BASIN::asStack, CreateIntegrationBlocks.EXTREME_BLAZE_BURNER::asStack),
                ExtremeMixingCategory::standard, 177, 103,
                new DoubleItemIcon(AllBlocks.MECHANICAL_MIXER::asStack,
                        CreateIntegrationBlocks.EXTREME_BLAZE_BURNER::asStack)));
        allCategories.add(build("liquid_burning", LiquidBurningRecipe.class,
                () -> typed(CreateIntegrationRecipeTypes.LIQUID_BURNING.getType()),
                List.<Supplier<? extends ItemStack>>of(CreateIntegrationBlocks.EXTREME_BLAZE_BURNER::asStack),
                LiquidBurningCategory::new, 177, 53,
                new ItemIcon(CreateIntegrationBlocks.EXTREME_BLAZE_BURNER::asStack)));
    }

    @SuppressWarnings("unchecked")
    private static <T extends Recipe<?>> List<RecipeHolder<T>> typed(
            net.minecraft.world.item.crafting.RecipeType<?> type) {
        List<RecipeHolder<T>> out = new ArrayList<>();
        CreateJEI.consumeTypedRecipes(holder -> out.add((RecipeHolder<T>) holder), type);
        return out;
    }

    @SuppressWarnings("unchecked")
    private <T extends Recipe<?>> CreateRecipeCategory<T> build(String name, Class<T> recipeClass,
            Supplier<List<RecipeHolder<T>>> recipes, List<Supplier<? extends ItemStack>> catalysts,
            CreateRecipeCategory.Factory<T> factory, int width, int height,
            mezz.jei.api.gui.drawable.IDrawable icon) {
        mezz.jei.api.recipe.RecipeType<RecipeHolder<T>> recipeType = (mezz.jei.api.recipe.RecipeType<RecipeHolder<T>>) (mezz.jei.api.recipe.RecipeType<?>) mezz.jei.api.recipe.RecipeType
                .create(AvaritiaIntegration.MOD_ID, name, RecipeHolder.class);
        CreateRecipeCategory.Info<T> info = new CreateRecipeCategory.Info<>(recipeType,
                Component.translatable(AvaritiaIntegration.MOD_ID + ".recipe." + name),
                new EmptyBackground(width, height), icon, recipes, catalysts);
        return factory.create(info);
    }

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        loadCategories();
        registration.addRecipeCategories(allCategories.toArray(IRecipeCategory[]::new));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        allCategories.forEach(c -> c.registerRecipes(registration));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        allCategories.forEach(c -> c.registerCatalysts(registration));
        addCatalyst(registration, CreateIntegrationBlocks.MATRIX_MECHANICAL_MIXER.asItem(), Create.asResource("mixing"),
                BasinRecipe.class);
        addCatalyst(registration, CreateIntegrationBlocks.NEUTRON_MECHANICAL_PRESS.asItem(),
                Create.asResource("pressing"), PressingRecipe.class);
        addCatalyst(registration, CreateIntegrationBlocks.EXTREME_BASIN.asItem(), Create.asResource("mixing"),
                BasinRecipe.class);
        addCatalyst(registration, CreateIntegrationBlocks.EXTREME_ENCASED_FAN.asItem(),
                Create.asResource("fan_washing"), SplashingRecipe.class);
        addCatalyst(registration, CreateIntegrationBlocks.EXTREME_ENCASED_FAN.asItem(),
                Create.asResource("fan_haunting"), HauntingRecipe.class);
        addCatalyst(registration, CreateIntegrationBlocks.EXTREME_ENCASED_FAN.asItem(),
                Create.asResource("fan_smoking"), SmokingRecipe.class);
        addCatalyst(registration, CreateIntegrationBlocks.EXTREME_ENCASED_FAN.asItem(),
                Create.asResource("fan_blasting"), AbstractCookingRecipe.class);
        registration.getJeiHelpers().getRecipeType(AllRecipeTypes.CRUSHING.getId())
                .ifPresent(type -> registration.addRecipeCatalyst(
                        CreateIntegrationBlocks.EXTREME_CRUSHING_WHEEL.asStack(), type));
        registration.getJeiHelpers().getRecipeType(AllRecipeTypes.DEPLOYING.getId())
                .ifPresent(type -> registration.addRecipeCatalyst(CreateIntegrationBlocks.EXTREME_DEPOT.asStack(),
                        type));
    }

    private <T extends Recipe<?>> void addCatalyst(IRecipeCatalystRegistration registration, ItemLike item,
            ResourceLocation recipeId, Class<? extends T> recipeClass) {
        registration.getJeiHelpers().getRecipeType(recipeId, recipeClass)
                .ifPresent(type -> registration.addRecipeCatalyst(new ItemStack(item), type));
    }
}
