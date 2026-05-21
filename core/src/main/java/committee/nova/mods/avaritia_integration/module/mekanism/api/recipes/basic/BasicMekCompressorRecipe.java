package committee.nova.mods.avaritia_integration.module.mekanism.api.recipes.basic;

import committee.nova.mods.avaritia_integration.module.mekanism.common.recipe.MekIntegrationRecipeType;
import committee.nova.mods.avaritia_integration.module.mekanism.common.registries.MekIntegrationBlocks;
import committee.nova.mods.avaritia_integration.module.mekanism.common.registries.MekIntegrationRecipeSerializers;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.basic.BasicItemStackToItemStackRecipe;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

@NothingNullByDefault
public class BasicMekCompressorRecipe extends BasicItemStackToItemStackRecipe {

    public BasicMekCompressorRecipe(ItemStackIngredient input, ItemStack output) {
        super(input, output, MekIntegrationRecipeType.MEK_COMPRESSING.getRecipeType());
    }

    @Override
    public RecipeSerializer<BasicMekCompressorRecipe> getSerializer() {
        return MekIntegrationRecipeSerializers.MEK_COMPRESSOR.get();
    }

    @Override
    public String getGroup() {
        return MekIntegrationBlocks.SINGULARITY_COMPRESSOR.getName();
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(MekIntegrationBlocks.SINGULARITY_COMPRESSOR.asItem());
    }
}
