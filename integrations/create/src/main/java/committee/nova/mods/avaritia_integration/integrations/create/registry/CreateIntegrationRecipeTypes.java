package committee.nova.mods.avaritia_integration.integrations.create.registry;

import java.util.Optional;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.integrations.create.compat.cca.liquid_burning.LiquidBurningRecipeSerializer;
import committee.nova.mods.avaritia_integration.integrations.create.content.recipe.ExtremeBasinRecipe;
import committee.nova.mods.avaritia_integration.integrations.create.content.recipe.ExtremeProcessingRecipe;
import committee.nova.mods.avaritia_integration.integrations.create.content.recipe.ExtremeProcessingRecipeSerializer;
import committee.nova.mods.avaritia_integration.integrations.create.content.recipe.mixer.ExtremeMixingRecipe;

import net.createmod.catnip.lang.Lang;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public enum CreateIntegrationRecipeTypes implements IRecipeTypeInfo {
    EXTREME_BASIN(ExtremeBasinRecipe::new),
    EXTREME_MIXING(ExtremeMixingRecipe::new),
    LIQUID_BURNING(LiquidBurningRecipeSerializer::new);

    private final ResourceLocation id;
    private final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> serializerObject;
    @Nullable
    private final DeferredHolder<RecipeType<?>, RecipeType<?>> typeObject;
    private final Supplier<RecipeType<?>> type;

    CreateIntegrationRecipeTypes(Supplier<RecipeSerializer<?>> serializerSupplier) {
        String name = Lang.asId(name());
        id = AvaritiaIntegration.rl(name);
        serializerObject = Registers.SERIALIZER_REGISTER.register(name, serializerSupplier);
        typeObject = Registers.TYPE_REGISTER.register(name, () -> RecipeType.simple(id));
        type = typeObject;
    }

    CreateIntegrationRecipeTypes(ExtremeProcessingRecipe.Factory processingFactory) {
        this(() -> new ExtremeProcessingRecipeSerializer<>(processingFactory));
    }

    public static void register(IEventBus modEventBus) {
        Registers.SERIALIZER_REGISTER.register(modEventBus);
        Registers.TYPE_REGISTER.register(modEventBus);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends RecipeSerializer<?>> T getSerializer() {
        return (T) serializerObject.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I extends RecipeInput, R extends Recipe<I>> RecipeType<R> getType() {
        return (RecipeType<R>) type.get();
    }

    public <I extends RecipeInput, R extends Recipe<I>> Optional<net.minecraft.world.item.crafting.RecipeHolder<R>> find(
            I inv, Level world) {
        return world.getRecipeManager().getRecipeFor(getType(), inv, world);
    }

    private static class Registers {
        private static final DeferredRegister<RecipeSerializer<?>> SERIALIZER_REGISTER = DeferredRegister
                .create(BuiltInRegistries.RECIPE_SERIALIZER, AvaritiaIntegration.MOD_ID);
        private static final DeferredRegister<RecipeType<?>> TYPE_REGISTER = DeferredRegister
                .create(Registries.RECIPE_TYPE, AvaritiaIntegration.MOD_ID);
    }
}
