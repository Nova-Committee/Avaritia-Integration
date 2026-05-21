package committee.nova.mods.avaritia_integration.module.mekanism.common.registries;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.module.mekanism.api.recipes.basic.BasicMekCompressorRecipe;
import committee.nova.mods.avaritia_integration.module.mekanism.api.recipes.basic.BasicNeutronCollectorRecipe;
import committee.nova.mods.avaritia_integration.module.mekanism.common.recipe.serializer.NeutronCollectorRecipeSerializer;
import mekanism.common.recipe.serializer.MekanismRecipeSerializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MekIntegrationRecipeSerializers {

    private MekIntegrationRecipeSerializers() {}

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, AvaritiaIntegration.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BasicNeutronCollectorRecipe>> COLLECTOR = RECIPE_SERIALIZERS.register("collector", () -> new NeutronCollectorRecipeSerializer<>(BasicNeutronCollectorRecipe::new));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BasicMekCompressorRecipe>> MEK_COMPRESSOR = RECIPE_SERIALIZERS.register("compressor", () -> MekanismRecipeSerializer.itemToItem(BasicMekCompressorRecipe::new));
}
