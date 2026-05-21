package committee.nova.mods.avaritia_integration.module.mekanism.common.recipe.serializer;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import committee.nova.mods.avaritia_integration.module.mekanism.api.recipes.chemicals.ChemicalStackToItemStackRecipe;
import mekanism.api.SerializationConstants;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

public class NeutronCollectorRecipeSerializer<RECIPE extends ChemicalStackToItemStackRecipe> implements RecipeSerializer<RECIPE> {

    private final MapCodec<RECIPE> codec;
    private final StreamCodec<RegistryFriendlyByteBuf, RECIPE> streamCodec;

    public NeutronCollectorRecipeSerializer(IFactory<RECIPE> factory) {
        this.codec = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ChemicalStackIngredient.CODEC.fieldOf(SerializationConstants.INPUT).forGetter(ChemicalStackToItemStackRecipe::getInput),
                ItemStack.CODEC.fieldOf(SerializationConstants.OUTPUT).forGetter(ChemicalStackToItemStackRecipe::getOutputRaw)
        ).apply(instance, factory::create));
        this.streamCodec = StreamCodec.composite(
                ChemicalStackIngredient.STREAM_CODEC, ChemicalStackToItemStackRecipe::getInput,
                ItemStack.STREAM_CODEC, ChemicalStackToItemStackRecipe::getOutputRaw,
                factory::create
        );
    }

    @Override
    public @NotNull MapCodec<RECIPE> codec() {
        return codec;
    }

    @Override
    public @NotNull StreamCodec<RegistryFriendlyByteBuf, RECIPE> streamCodec() {
        return streamCodec;
    }

    @FunctionalInterface
    public interface IFactory<RECIPE extends ChemicalStackToItemStackRecipe> {
        RECIPE create(ChemicalStackIngredient input, ItemStack output);
    }
}
