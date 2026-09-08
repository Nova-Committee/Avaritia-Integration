package committee.nova.mods.avaritia_integration.integrations.create.content.recipe;

import com.mojang.serialization.MapCodec;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ExtremeProcessingRecipeSerializer<T extends ExtremeProcessingRecipe> implements RecipeSerializer<T> {
    private final ExtremeProcessingRecipe.Factory factory;

    public ExtremeProcessingRecipeSerializer(ExtremeProcessingRecipe.Factory factory) {
        this.factory = factory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public MapCodec<T> codec() {
        return (MapCodec<T>) ExtremeProcessingRecipe.codec(factory);
    }

    @Override
    @SuppressWarnings("unchecked")
    public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
        return (StreamCodec<RegistryFriendlyByteBuf, T>) ExtremeProcessingRecipe.streamCodec(factory);
    }
}
