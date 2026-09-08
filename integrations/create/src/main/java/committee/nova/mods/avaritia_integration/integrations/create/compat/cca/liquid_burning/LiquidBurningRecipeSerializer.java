package committee.nova.mods.avaritia_integration.integrations.create.compat.cca.liquid_burning;

import com.mojang.serialization.MapCodec;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class LiquidBurningRecipeSerializer implements RecipeSerializer<LiquidBurningRecipe> {
    @Override
    public MapCodec<LiquidBurningRecipe> codec() {
        return LiquidBurningRecipe.CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, LiquidBurningRecipe> streamCodec() {
        return LiquidBurningRecipe.STREAM_CODEC;
    }
}
