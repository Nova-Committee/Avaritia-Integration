package committee.nova.mods.avaritia_integration.integrations.mekanism.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeInput;

import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import mekanism.common.registration.impl.RecipeTypeRegistryObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Function;

@Mixin(value = MekanismRecipeType.class, remap = false)
public interface MixinMekanismRecipeType {

    @Invoker("register")
    static <INPUT extends RecipeInput, RECIPE extends MekanismRecipe<INPUT>,
            CACHE extends IInputRecipeCache> RecipeTypeRegistryObject<INPUT, RECIPE, CACHE> avaritia$register(ResourceLocation name,
                                                                                                              Function<MekanismRecipeType<INPUT, RECIPE, CACHE>, CACHE> cacheCreator) {
        throw new AssertionError();
    }
}
