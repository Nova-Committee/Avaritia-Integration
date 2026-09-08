package committee.nova.mods.avaritia_integration.integrations.create.compat.cca.liquid_burning;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;


import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationRecipeTypes;

public record LiquidBurningRecipe(SizedFluidIngredient input, int burnTime, boolean starheated)
        implements Recipe<FluidRecipeInput> {

    public static final MapCodec<LiquidBurningRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SizedFluidIngredient.FLAT_CODEC.fieldOf("input").forGetter(LiquidBurningRecipe::input),
            Codec.INT.fieldOf("burnTime").forGetter(LiquidBurningRecipe::burnTime),
            Codec.BOOL.optionalFieldOf("starheated", false).forGetter(LiquidBurningRecipe::starheated)
    ).apply(instance, LiquidBurningRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, LiquidBurningRecipe> STREAM_CODEC = StreamCodec.composite(
            SizedFluidIngredient.STREAM_CODEC, LiquidBurningRecipe::input,
            ByteBufCodecs.VAR_INT, LiquidBurningRecipe::burnTime,
            ByteBufCodecs.BOOL, LiquidBurningRecipe::starheated,
            LiquidBurningRecipe::new);

    @Override
    public boolean matches(FluidRecipeInput input, Level level) {
        return this.input.test(input.fluid());
    }

    @Override
    public ItemStack assemble(FluidRecipeInput input, HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CreateIntegrationRecipeTypes.LIQUID_BURNING.getSerializer();
    }

    @Override
    public RecipeType<?> getType() {
        return CreateIntegrationRecipeTypes.LIQUID_BURNING.getType();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
}
