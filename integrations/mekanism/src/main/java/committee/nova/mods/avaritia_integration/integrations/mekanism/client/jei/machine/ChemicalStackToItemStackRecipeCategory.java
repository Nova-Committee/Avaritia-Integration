package committee.nova.mods.avaritia_integration.integrations.mekanism.client.jei.machine;

import committee.nova.mods.avaritia_integration.integrations.mekanism.api.recipes.chemicals.ChemicalStackToItemStackRecipe;

import net.minecraft.world.item.crafting.RecipeHolder;

import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiChemicalGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.recipe_viewer.jei.HolderRecipeCategory;
import mekanism.client.recipe_viewer.type.IRecipeViewerRecipeType;
import mekanism.common.tile.component.config.DataType;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import org.jetbrains.annotations.NotNull;

public class ChemicalStackToItemStackRecipeCategory extends HolderRecipeCategory<ChemicalStackToItemStackRecipe> {

    private static final String CHEMICAL_INPUT = "chemicalInput";

    private final GuiGauge<?> input;
    private final GuiSlot output;

    public ChemicalStackToItemStackRecipeCategory(IGuiHelper helper,
                                                  IRecipeViewerRecipeType<ChemicalStackToItemStackRecipe> recipeType) {
        super(helper, recipeType);
        input = addElement(GuiChemicalGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT), this, 26, 13));
        output = addSlot(SlotType.OUTPUT, 131, 36);
        addSimpleProgress(ProgressType.LARGE_RIGHT, 64, 40);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder,
                          RecipeHolder<ChemicalStackToItemStackRecipe> recipeHolder, @NotNull IFocusGroup focusGroup) {
        ChemicalStackToItemStackRecipe recipe = recipeHolder.value();
        initChemical(builder, RecipeIngredientRole.INPUT, input, recipe.getInput().getRepresentations())
                .setSlotName(CHEMICAL_INPUT);
        initItem(builder, RecipeIngredientRole.OUTPUT, output, recipe.getOutputDefinition());
    }
}
