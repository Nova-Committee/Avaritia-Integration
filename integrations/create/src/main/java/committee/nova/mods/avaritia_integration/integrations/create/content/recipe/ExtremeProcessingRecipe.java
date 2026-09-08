package committee.nova.mods.avaritia_integration.integrations.create.content.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Joiner;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class ExtremeProcessingRecipe implements Recipe<RecipeInput> {
    protected final ExtremeProcessingRecipeParams params;
    protected final NonNullList<Ingredient> ingredients;
    protected final NonNullList<ProcessingOutput> results;
    protected final NonNullList<SizedFluidIngredient> fluidIngredients;
    protected final NonNullList<FluidStack> fluidResults;
    protected final int processingDuration;
    protected final ExtremeHeatCondition requiredHeat;
    private final RecipeType<?> type;
    private final RecipeSerializer<?> serializer;
    private final IRecipeTypeInfo typeInfo;
    private Supplier<ItemStack> forcedResult;

    public ExtremeProcessingRecipe(IRecipeTypeInfo typeInfo, ExtremeProcessingRecipeParams params) {
        this.params = params;
        this.ingredients = params.ingredients;
        this.fluidIngredients = params.fluidIngredients;
        this.results = params.results;
        this.fluidResults = params.fluidResults;
        this.processingDuration = params.processingDuration;
        this.requiredHeat = params.requiredHeat;
        this.type = typeInfo.getType();
        this.serializer = typeInfo.getSerializer();
        this.typeInfo = typeInfo;
    }

    protected abstract int getMaxInputCount();

    protected abstract int getMaxOutputCount();

    protected boolean canRequireHeat() {
        return false;
    }

    protected boolean canSpecifyDuration() {
        return false;
    }

    protected int getMaxFluidInputCount() {
        return 0;
    }

    protected int getMaxFluidOutputCount() {
        return 0;
    }

    public ExtremeProcessingRecipeParams getParams() {
        return params;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    public NonNullList<SizedFluidIngredient> getFluidIngredients() {
        return fluidIngredients;
    }

    public List<ProcessingOutput> getRollableResults() {
        return results;
    }

    public NonNullList<FluidStack> getFluidResults() {
        return fluidResults;
    }

    public List<ItemStack> rollResults(RandomSource randomSource) {
        List<ItemStack> rolled = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            ItemStack stack = i == 0 && forcedResult != null ? forcedResult.get() : results.get(i).rollOutput(randomSource);
            if (!stack.isEmpty()) {
                rolled.add(stack);
            }
        }
        return rolled;
    }

    public int getProcessingDuration() {
        return processingDuration;
    }

    public ExtremeHeatCondition getRequiredHeat() {
        return requiredHeat;
    }

    @Override
    public ItemStack assemble(RecipeInput input, HolderLookup.Provider provider) {
        return getResultItem(provider);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return results.isEmpty() ? ItemStack.EMPTY : results.getFirst().getStack();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public String getGroup() {
        return "extreme_processing";
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return serializer;
    }

    @Override
    public RecipeType<?> getType() {
        return type;
    }

    public IRecipeTypeInfo getTypeInfo() {
        return typeInfo;
    }

    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (ingredients.size() > getMaxInputCount()) {
            errors.add("Too many item inputs");
        }
        if (results.size() > getMaxOutputCount()) {
            errors.add("Too many item outputs");
        }
        if (fluidIngredients.size() > getMaxFluidInputCount()) {
            errors.add("Too many fluid inputs");
        }
        if (fluidResults.size() > getMaxFluidOutputCount()) {
            errors.add("Too many fluid outputs");
        }
        return errors;
    }

    public static MapCodec<ExtremeProcessingRecipe> codec(Factory factory) {
        return ExtremeProcessingRecipeParams.CODEC.xmap(factory::create, ExtremeProcessingRecipe::getParams)
                .validate(recipe -> {
                    List<String> errors = recipe.validate();
                    if (errors.isEmpty()) {
                        return DataResult.success(recipe);
                    }
                    errors.add(recipe.getClass().getSimpleName() + " failed validation:");
                    return DataResult.error(() -> Joiner.on('\n').join(errors), recipe);
                });
    }

    public static StreamCodec<RegistryFriendlyByteBuf, ExtremeProcessingRecipe> streamCodec(Factory factory) {
        return ExtremeProcessingRecipeParams.STREAM_CODEC.map(factory::create, ExtremeProcessingRecipe::getParams);
    }

    @FunctionalInterface
    public interface Factory {
        ExtremeProcessingRecipe create(ExtremeProcessingRecipeParams params);
    }
}
