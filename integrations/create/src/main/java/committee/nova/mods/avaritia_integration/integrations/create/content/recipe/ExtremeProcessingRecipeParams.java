package committee.nova.mods.avaritia_integration.integrations.create.content.recipe;

import java.util.ArrayList;
import java.util.List;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.codec.CreateCodecs;

import net.createmod.catnip.codecs.stream.CatnipStreamCodecBuilders;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;

import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

public class ExtremeProcessingRecipeParams {
    public static final MapCodec<ExtremeProcessingRecipeParams> CODEC = codec();
    public static final StreamCodec<RegistryFriendlyByteBuf, ExtremeProcessingRecipeParams> STREAM_CODEC = streamCodec();

    protected NonNullList<Ingredient> ingredients = NonNullList.create();
    protected NonNullList<ProcessingOutput> results = NonNullList.create();
    protected NonNullList<SizedFluidIngredient> fluidIngredients = NonNullList.create();
    protected NonNullList<FluidStack> fluidResults = NonNullList.create();
    protected int processingDuration;
    protected ExtremeHeatCondition requiredHeat = ExtremeHeatCondition.NORMAL;

    private static MapCodec<ExtremeProcessingRecipeParams> codec() {
        Codec<Either<SizedFluidIngredient, SizedIngredient>> ingredientCodec = Codec.either(
                CreateCodecs.FLAT_SIZED_FLUID_INGREDIENT_WITH_TYPE, SizedIngredient.FLAT_CODEC);
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                ingredientCodec.listOf().fieldOf("ingredients").forGetter(ExtremeProcessingRecipeParams::ingredients),
                Codec.either(FluidStack.CODEC, ProcessingOutput.CODEC_NEW).listOf().fieldOf("results")
                        .forGetter(ExtremeProcessingRecipeParams::results),
                Codec.INT.optionalFieldOf("processingTime", 0)
                        .forGetter(params -> params.processingDuration),
                Codec.INT.optionalFieldOf("processing_time", 0)
                        .forGetter(params -> params.processingDuration),
                ExtremeHeatCondition.CODEC.optionalFieldOf("heatRequirement", ExtremeHeatCondition.NORMAL)
                        .forGetter(params -> params.requiredHeat),
                ExtremeHeatCondition.CODEC.optionalFieldOf("heat_requirement", ExtremeHeatCondition.NORMAL)
                        .forGetter(params -> params.requiredHeat)
        ).apply(instance, (ingredients, results, processingTime, processingTimeAlt, heat, heatAlt) -> {
            ExtremeProcessingRecipeParams params = new ExtremeProcessingRecipeParams();
            ingredients.forEach(either -> either
                    .ifLeft(params.fluidIngredients::add)
                    .ifRight(sized -> {
                        for (int i = 0; i < Math.max(1, sized.count()); i++) {
                            params.ingredients.add(sized.ingredient());
                        }
                    }));
            results.forEach(either -> either
                    .ifLeft(params.fluidResults::add)
                    .ifRight(params.results::add));
            params.processingDuration = processingTime > 0 ? processingTime : processingTimeAlt;
            params.requiredHeat = heat != ExtremeHeatCondition.NORMAL ? heat : heatAlt;
            return params;
        }));
    }

    private static StreamCodec<RegistryFriendlyByteBuf, ExtremeProcessingRecipeParams> streamCodec() {
        return StreamCodec.of((buffer, params) -> {
            CatnipStreamCodecBuilders.nonNullList(Ingredient.CONTENTS_STREAM_CODEC).encode(buffer, params.ingredients);
            CatnipStreamCodecBuilders.nonNullList(SizedFluidIngredient.STREAM_CODEC).encode(buffer, params.fluidIngredients);
            CatnipStreamCodecBuilders.nonNullList(ProcessingOutput.STREAM_CODEC).encode(buffer, params.results);
            CatnipStreamCodecBuilders.nonNullList(FluidStack.STREAM_CODEC).encode(buffer, params.fluidResults);
            ByteBufCodecs.VAR_INT.encode(buffer, params.processingDuration);
            ExtremeHeatCondition.STREAM_CODEC.encode(buffer, params.requiredHeat);
        }, buffer -> {
            ExtremeProcessingRecipeParams params = new ExtremeProcessingRecipeParams();
            params.ingredients = CatnipStreamCodecBuilders.nonNullList(Ingredient.CONTENTS_STREAM_CODEC).decode(buffer);
            params.fluidIngredients = CatnipStreamCodecBuilders.nonNullList(SizedFluidIngredient.STREAM_CODEC).decode(buffer);
            params.results = CatnipStreamCodecBuilders.nonNullList(ProcessingOutput.STREAM_CODEC).decode(buffer);
            params.fluidResults = CatnipStreamCodecBuilders.nonNullList(FluidStack.STREAM_CODEC).decode(buffer);
            params.processingDuration = ByteBufCodecs.VAR_INT.decode(buffer);
            params.requiredHeat = ExtremeHeatCondition.STREAM_CODEC.decode(buffer);
            return params;
        });
    }

    private List<Either<SizedFluidIngredient, SizedIngredient>> ingredients() {
        List<Either<SizedFluidIngredient, SizedIngredient>> list = new ArrayList<>();
        ingredients.forEach(ingredient -> list.add(Either.right(new SizedIngredient(ingredient, 1))));
        fluidIngredients.forEach(ingredient -> list.add(Either.left(ingredient)));
        return list;
    }

    private List<Either<FluidStack, ProcessingOutput>> results() {
        List<Either<FluidStack, ProcessingOutput>> list = new ArrayList<>();
        results.forEach(result -> list.add(Either.right(result)));
        fluidResults.forEach(result -> list.add(Either.left(result)));
        return list;
    }
}
