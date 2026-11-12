package committee.nova.mods.avaritia_integration.integrations.create.mixin;

import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationRecipeTypes;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MechanicalMixerBlockEntity.class)
public class MixinMechanicalMixerBlockEntity {

    @Inject(method = "matchStaticFilters", at = @At("HEAD"), cancellable = true, remap = false)
    private void avaritia$allowExtremeMixing(RecipeHolder<? extends Recipe<?>> recipe,
                                             CallbackInfoReturnable<Boolean> cir) {
        if (committee.nova.mods.avaritia_integration.api.load.IntegrationLoadApi
                .shouldLoad("avaritia_integration_create") &&
                recipe.value().getType() == CreateIntegrationRecipeTypes.EXTREME_MIXING.getType()) {
            cir.setReturnValue(true);
        }
    }
}
