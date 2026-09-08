package committee.nova.mods.avaritia_integration.integrations.create.compat.jei.category;

import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.gui.AllGuiTextures;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.integrations.create.compat.cca.liquid_burning.LiquidBurningRecipe;
import committee.nova.mods.avaritia_integration.integrations.create.compat.jei.category.animations.AnimatedExtremeBlazeBurner;
import committee.nova.mods.avaritia_integration.integrations.create.content.recipe.ExtremeHeatCondition;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class LiquidBurningCategory extends CreateRecipeCategory<LiquidBurningRecipe> {

    private final AnimatedExtremeBlazeBurner heater = new AnimatedExtremeBlazeBurner();

    public LiquidBurningCategory(Info<LiquidBurningRecipe> info) {
        super(info);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, LiquidBurningRecipe recipe, IFocusGroup focuses) {
        addFluidSlot(builder, getBackground().getWidth() / 2 - 16, 3, recipe.input());
    }

    @Override
    public void draw(LiquidBurningRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX,
            double mouseY) {
        graphics.drawString(Minecraft.getInstance().font, formatTime(recipe.burnTime()),
                getBackground().getWidth() / 2 + 48, 36, 4210752, false);
        ExtremeHeatCondition requiredHeat = recipe.starheated() ? ExtremeHeatCondition.STAR : ExtremeHeatCondition.BLAZE;
        AllGuiTextures.JEI_LIGHT.render(graphics, 81, 38);
        AllGuiTextures.JEI_HEAT_BAR.render(graphics, 4, 30);
        graphics.drawString(Minecraft.getInstance().font,
                Component.translatable(AvaritiaIntegration.MOD_ID + "." + requiredHeat.getTranslationKey()), 9, 36,
                requiredHeat.getColor(), false);
        heater.withHeat(requiredHeat.visualizeAsBlazeBurner()).draw(graphics, getBackground().getWidth() / 2 + 3, 5);
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, getBackground().getWidth() / 2 + 3, 8);
    }

    private static String formatTime(int ticks) {
        if (ticks > 20 * 60) {
            return (ticks / (20 * 60)) + " min";
        }
        if (ticks > 20) {
            return (ticks / 20) + " sec";
        }
        return ticks + " ticks";
    }
}
