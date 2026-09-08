package committee.nova.mods.avaritia_integration.integrations.create.compat.jei.category;

import com.simibubi.create.compat.jei.category.animations.AnimatedMixer;

import committee.nova.mods.avaritia_integration.integrations.create.compat.jei.category.animations.AnimatedExtremeBlazeBurner;
import committee.nova.mods.avaritia_integration.integrations.create.content.recipe.ExtremeBasinRecipe;
import committee.nova.mods.avaritia_integration.integrations.create.content.recipe.ExtremeHeatCondition;

import net.minecraft.client.gui.GuiGraphics;

import mezz.jei.api.gui.ingredient.IRecipeSlotsView;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ExtremeMixingCategory extends ExtremeBasinCategory {

    private final AnimatedMixer mixer = new AnimatedMixer();
    private final AnimatedExtremeBlazeBurner heater = new AnimatedExtremeBlazeBurner();

    public static ExtremeMixingCategory standard(Info<ExtremeBasinRecipe> info) {
        return new ExtremeMixingCategory(info);
    }

    protected ExtremeMixingCategory(Info<ExtremeBasinRecipe> info) {
        super(info, true);
    }

    @Override
    public void draw(ExtremeBasinRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX,
            double mouseY) {
        super.draw(recipe, iRecipeSlotsView, graphics, mouseX, mouseY);
        ExtremeHeatCondition requiredHeat = recipe.getRequiredHeat();
        heater.withHeat(requiredHeat.visualizeAsBlazeBurner()).draw(graphics, getBackground().getWidth() / 2 + 3, 55);
        mixer.draw(graphics, getBackground().getWidth() / 2 + 3, 34);
    }
}
