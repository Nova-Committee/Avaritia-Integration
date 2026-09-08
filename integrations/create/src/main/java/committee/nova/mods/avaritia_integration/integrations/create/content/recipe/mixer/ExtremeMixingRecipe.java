package committee.nova.mods.avaritia_integration.integrations.create.content.recipe.mixer;

import committee.nova.mods.avaritia_integration.integrations.create.content.recipe.ExtremeBasinRecipe;
import committee.nova.mods.avaritia_integration.integrations.create.content.recipe.ExtremeProcessingRecipeParams;
import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationRecipeTypes;

public class ExtremeMixingRecipe extends ExtremeBasinRecipe {
    public ExtremeMixingRecipe(ExtremeProcessingRecipeParams params) {
        super(CreateIntegrationRecipeTypes.EXTREME_MIXING, params);
    }
}
