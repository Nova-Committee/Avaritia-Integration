package committee.nova.mods.avaritia_integration.integrations.mekanism.client.jei;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.integrations.mekanism.client.jei.machine.ChemicalStackToItemStackRecipeCategory;
import committee.nova.mods.avaritia_integration.integrations.mekanism.client.recipe_viewer.MekIntegrationRecipeViewerTypes;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.recipe.MekIntegrationRecipeType;

import net.minecraft.resources.ResourceLocation;

import mekanism.client.recipe_viewer.jei.CatalystRegistryHelper;
import mekanism.client.recipe_viewer.jei.RecipeRegistryHelper;
import mekanism.client.recipe_viewer.jei.machine.ItemStackToItemStackRecipeCategory;
import mekanism.common.Mekanism;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class MekIntegrationJEI implements IModPlugin {

    public MekIntegrationJEI() {
        AvaritiaIntegration.LOGGER.info("MekIntegrationJEI constructed (JEI scan discovery)");
    }

    private static boolean shouldLoad() {
        return !Mekanism.hooks.emi.isLoaded();
    }

    @NotNull
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(AvaritiaIntegration.MOD_ID, "mekanism_jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        if (!shouldLoad()) {
            return;
        }
        AvaritiaIntegration.LOGGER.info("MekIntegrationJEI.registerCategories");
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(
                new ChemicalStackToItemStackRecipeCategory(guiHelper, MekIntegrationRecipeViewerTypes.COLLECTING),
                new ItemStackToItemStackRecipeCategory(guiHelper, MekIntegrationRecipeViewerTypes.COMPRESSING));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        if (!shouldLoad()) {
            return;
        }
        RecipeRegistryHelper.register(registry, MekIntegrationRecipeViewerTypes.COLLECTING,
                MekIntegrationRecipeType.COLLECTING);
        RecipeRegistryHelper.register(registry, MekIntegrationRecipeViewerTypes.COMPRESSING,
                MekIntegrationRecipeType.MEK_COMPRESSING);
        AvaritiaIntegration.LOGGER.info("Registered neutron collector JEI recipes ({})",
                MekIntegrationRecipeType.COLLECTING.getRecipes().size());
        AvaritiaIntegration.LOGGER.info("Registered singularity compressor JEI recipes ({})",
                MekIntegrationRecipeType.MEK_COMPRESSING.getRecipes().size());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        if (!shouldLoad()) {
            return;
        }
        CatalystRegistryHelper.register(registry, MekIntegrationRecipeViewerTypes.COLLECTING,
                MekIntegrationRecipeViewerTypes.COMPRESSING);
    }
}
