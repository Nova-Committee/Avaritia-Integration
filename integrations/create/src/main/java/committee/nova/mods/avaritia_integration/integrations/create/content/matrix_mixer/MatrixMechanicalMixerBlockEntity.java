package committee.nova.mods.avaritia_integration.integrations.create.content.matrix_mixer;

import java.util.List;
import java.util.Optional;

import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;

import committee.nova.mods.avaritia_integration.integrations.create.content.recipe.ExtremeBasinRecipe;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class MatrixMechanicalMixerBlockEntity extends MechanicalMixerBlockEntity {
    public MatrixMechanicalMixerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected <I extends RecipeInput> boolean matchBasinRecipe(Recipe<I> recipe) {
        if (recipe instanceof ExtremeBasinRecipe) {
            Optional<BasinBlockEntity> basin = getBasin();
            return basin.map(value -> ExtremeBasinRecipe.match(value, recipe)).orElse(false);
        }
        return super.matchBasinRecipe(recipe);
    }

    @Override
    protected void applyBasinRecipe() {
        if (currentRecipe instanceof ExtremeBasinRecipe) {
            Optional<BasinBlockEntity> basin = getBasin();
            if (basin.isEmpty()) {
                return;
            }
            ExtremeBasinRecipe.apply(basin.get(), currentRecipe);
            basin.get().notifyChangeOfContents();
            sendData();
            return;
        }
        super.applyBasinRecipe();
    }

    @Override
    protected List<Recipe<?>> getMatchingRecipes() {
        List<Recipe<?>> recipes = super.getMatchingRecipes();
        getBasin().ifPresent(basin -> {
            level.getRecipeManager().getAllRecipesFor(
                    committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationRecipeTypes.EXTREME_MIXING.getType())
                    .stream()
                    .map(RecipeHolder::value)
                    .filter(recipe -> ExtremeBasinRecipe.match(basin, recipe))
                    .forEach(recipes::add);
        });
        return recipes;
    }
}
