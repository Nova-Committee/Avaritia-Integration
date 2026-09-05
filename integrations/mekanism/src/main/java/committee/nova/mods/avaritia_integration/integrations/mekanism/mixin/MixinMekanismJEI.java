package committee.nova.mods.avaritia_integration.integrations.mekanism.mixin;

import committee.nova.mods.avaritia_integration.integrations.mekanism.client.jei.MekIntegrationJEI;

import mekanism.client.recipe_viewer.jei.MekanismJEI;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MekanismJEI.class, remap = false)
public abstract class MixinMekanismJEI {

    @Inject(method = "registerCategories", at = @At("RETURN"))
    private void avaritia_integration$registerCompressorCategory(IRecipeCategoryRegistration registry,
                                                                 CallbackInfo ci) {
        MekIntegrationJEI.registerCompressor(registry);
    }

    @Inject(method = "registerRecipes", at = @At("RETURN"))
    private void avaritia_integration$registerCompressorRecipes(IRecipeRegistration registry, CallbackInfo ci) {
        MekIntegrationJEI.registerCompressor(registry);
    }

    @Inject(method = "registerRecipeCatalysts", at = @At("RETURN"))
    private void avaritia_integration$registerCompressorCatalysts(IRecipeCatalystRegistration registry,
                                                                  CallbackInfo ci) {
        MekIntegrationJEI.registerCompressor(registry);
    }
}
