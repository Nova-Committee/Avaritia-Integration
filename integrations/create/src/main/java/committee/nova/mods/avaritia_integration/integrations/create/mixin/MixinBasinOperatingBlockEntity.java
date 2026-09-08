package committee.nova.mods.avaritia_integration.integrations.create.mixin;

import java.util.Optional;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import com.simibubi.create.content.processing.basin.BasinRecipe;

import committee.nova.mods.avaritia_integration.integrations.create.content.recipe.ExtremeBasinRecipe;

import net.minecraft.world.item.crafting.Recipe;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BasinOperatingBlockEntity.class)
public class MixinBasinOperatingBlockEntity {

    @Inject(method = "matchBasinRecipe", at = @At("RETURN"), cancellable = true, remap = false)
    private void avaritia$matchExtremeHeat(Recipe<?> recipe, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() || !(recipe instanceof ExtremeBasinRecipe)) {
            return;
        }
        Optional<BasinBlockEntity> basin = ((BasinOperatingBlockEntityAccessor) this).avaritia$getBasin();
        cir.setReturnValue(basin.isPresent() && ExtremeBasinRecipe.match(basin.get(), recipe));
    }

    @Redirect(method = "applyBasinRecipe", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/processing/basin/BasinRecipe;apply(Lcom/simibubi/create/content/processing/basin/BasinBlockEntity;Lnet/minecraft/world/item/crafting/Recipe;)Z"), remap = false)
    private boolean avaritia$applyExtremeBasin(BasinBlockEntity basin, Recipe<?> recipe) {
        if (recipe instanceof ExtremeBasinRecipe) {
            return ExtremeBasinRecipe.apply(basin, recipe);
        }
        return BasinRecipe.apply(basin, recipe);
    }
}
