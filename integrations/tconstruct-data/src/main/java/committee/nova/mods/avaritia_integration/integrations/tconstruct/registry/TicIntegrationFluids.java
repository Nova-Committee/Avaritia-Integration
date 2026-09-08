package committee.nova.mods.avaritia_integration.integrations.tconstruct.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class TicIntegrationFluids {

    public static final DeferredRegister<Fluid> REGISTRY = DeferredRegister.create(BuiltInRegistries.FLUID,
            AvaritiaIntegration.MOD_ID);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> SOURCE_MOLTEN_INFINITY = REGISTRY.register(
            "molten_infinity",
            () -> new BaseFlowingFluid.Source(TicIntegrationFluids.MOLTEN_INFINITY));
    public static final DeferredHolder<Fluid, FlowingFluid> FLOWING_MOLTEN_INFINITY = REGISTRY.register(
            "flowing_molten_infinity",
            () -> new BaseFlowingFluid.Flowing(TicIntegrationFluids.MOLTEN_INFINITY));

    public static final BaseFlowingFluid.Properties MOLTEN_INFINITY = new BaseFlowingFluid.Properties(
            TicIntegrationFluidTypes.MOLTEN_INFINITY, SOURCE_MOLTEN_INFINITY, FLOWING_MOLTEN_INFINITY)
            .slopeFindDistance(3).levelDecreasePerBlock(3).tickRate(30)
            .block(TicIntegrationBlocks.MOLTEN_INFINITY_FLUID)
            .bucket(TicIntegrationItems.MOLTEN_INFINITY_BUCKET);

    private TicIntegrationFluids() {}
}
