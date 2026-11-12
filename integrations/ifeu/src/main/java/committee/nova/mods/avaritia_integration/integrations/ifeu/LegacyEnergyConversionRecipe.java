package committee.nova.mods.avaritia_integration.integrations.ifeu;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.neoforged.neoforge.registries.DeferredRegister;

import com.mojang.serialization.MapCodec;

import java.util.function.Supplier;

public final class LegacyEnergyConversionRecipe extends ShapelessRecipe {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(
            Registries.RECIPE_SERIALIZER, AvaritiaIntegration.MOD_ID);
    private static final Supplier<RecipeSerializer<LegacyEnergyConversionRecipe>> SERIALIZER = SERIALIZERS.register(
            "legacy_energy_conversion", () -> new RecipeSerializer<>() {

                @Override
                public MapCodec<LegacyEnergyConversionRecipe> codec() {
                    return RecipeSerializer.SHAPELESS_RECIPE.codec().xmap(LegacyEnergyConversionRecipe::new,
                            recipe -> recipe);
                }

                @Override
                public StreamCodec<RegistryFriendlyByteBuf, LegacyEnergyConversionRecipe> streamCodec() {
                    return ShapelessRecipe.Serializer.STREAM_CODEC.map(LegacyEnergyConversionRecipe::new,
                            recipe -> recipe);
                }
            });

    public LegacyEnergyConversionRecipe(ShapelessRecipe recipe) {
        super(recipe.getGroup(), recipe.category(), recipe.getResultItem(null), recipe.getIngredients());
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack result = super.assemble(input, registries);
        for (ItemStack stack : input.items()) {
            if (!stack.isEmpty()) {
                result.applyComponents(stack.getComponentsPatch());
                break;
            }
        }
        return result;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        return NonNullList.withSize(input.size(), ItemStack.EMPTY);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER.get();
    }
}
