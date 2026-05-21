package committee.nova.mods.avaritia_integration.integrations.mekanism.api.recipes.basic;

import committee.nova.mods.avaritia_integration.integrations.mekanism.api.recipes.chemicals.ChemicalStackToItemStackRecipe;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.recipe.MekIntegrationRecipeType;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.registries.MekIntegrationBlocks;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.registries.MekIntegrationRecipeSerializers;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;

@NothingNullByDefault
public class BasicNeutronCollectorRecipe extends ChemicalStackToItemStackRecipe {

    public BasicNeutronCollectorRecipe(ChemicalStackIngredient input, ItemStack output) {
        super(input, output);
    }

    @Override
    public RecipeSerializer<BasicNeutronCollectorRecipe> getSerializer() {
        return MekIntegrationRecipeSerializers.COLLECTOR.get();
    }

    @Override
    public RecipeType<ChemicalStackToItemStackRecipe> getType() {
        return MekIntegrationRecipeType.COLLECTING.getRecipeType();
    }

    @Override
    public String getGroup() {
        return MekIntegrationBlocks.NEUTRON_COLLECTOR.getName();
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(MekIntegrationBlocks.NEUTRON_COLLECTOR.asItem());
    }
}
