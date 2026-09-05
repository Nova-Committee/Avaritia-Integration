package committee.nova.mods.avaritia_integration.integrations.mekanism.client.jei;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.integrations.mekanism.client.recipe_viewer.MekIntegrationRecipeViewerTypes;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.recipe.MekIntegrationRecipeType;

import mekanism.client.recipe_viewer.jei.CatalystRegistryHelper;
import mekanism.client.recipe_viewer.jei.RecipeRegistryHelper;
import mekanism.client.recipe_viewer.jei.machine.ItemStackToItemStackRecipeCategory;
import mekanism.common.Mekanism;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;

public final class MekIntegrationJEI {

    private MekIntegrationJEI() {}

    private static boolean shouldLoad() {
        return !Mekanism.hooks.emi.isLoaded();
    }

    public static void registerCompressor(IRecipeCategoryRegistration registry) {
        if (!shouldLoad()) {
            return;
        }
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(
                new ItemStackToItemStackRecipeCategory(guiHelper, MekIntegrationRecipeViewerTypes.COMPRESSING));
    }

    public static void registerCompressor(IRecipeRegistration registry) {
        if (!shouldLoad()) {
            return;
        }
        RecipeRegistryHelper.register(registry, MekIntegrationRecipeViewerTypes.COMPRESSING,
                MekIntegrationRecipeType.MEK_COMPRESSING);
        AvaritiaIntegration.LOGGER.info("Registered singularity compressor JEI recipes ({})",
                MekIntegrationRecipeType.MEK_COMPRESSING.getRecipes().size());
    }

    public static void registerCompressor(IRecipeCatalystRegistration registry) {
        if (!shouldLoad()) {
            return;
        }
        CatalystRegistryHelper.register(registry, MekIntegrationRecipeViewerTypes.COMPRESSING);
    }
}
