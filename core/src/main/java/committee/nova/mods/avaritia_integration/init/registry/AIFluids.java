package committee.nova.mods.avaritia_integration.init.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AIFluids {
    public static final DeferredRegister<Fluid> REGISTRY = DeferredRegister.create(BuiltInRegistries.FLUID, AvaritiaIntegration.MOD_ID);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> source_molten_blaze = REGISTRY.register("molten_blaze",
            ()->new BaseFlowingFluid.Source(AIFluids.molten_blaze));
    public static final DeferredHolder<Fluid, FlowingFluid> flowing_molten_blaze = REGISTRY.register("flowing_molten_blaze",
            ()->new BaseFlowingFluid.Flowing(AIFluids.molten_blaze));
    public static final BaseFlowingFluid.Properties molten_blaze = new BaseFlowingFluid.Properties(
            AIFluidTypes.molten_blaze, source_molten_blaze, flowing_molten_blaze).slopeFindDistance(3).levelDecreasePerBlock(3).tickRate(30)
            .block(AIBlocks.molten_blaze_block).bucket(AIItems.MOLTEN_BLAZE_BUCKET);

    public static final DeferredHolder<Fluid,BaseFlowingFluid.Source> source_molten_crystal_matrix = REGISTRY.register("molten_crystal_matrix",
            ()->new BaseFlowingFluid.Source(AIFluids.molten_crystal_matrix));
    public static final DeferredHolder<Fluid,BaseFlowingFluid> flowing_molten_crystal_matrix = REGISTRY.register("flowing_molten_crystal_matrix",
            ()->new BaseFlowingFluid.Flowing(AIFluids.molten_crystal_matrix));
    public static final BaseFlowingFluid.Properties molten_crystal_matrix = new BaseFlowingFluid.Properties(
            AIFluidTypes.molten_crystal_matrix, source_molten_crystal_matrix, flowing_molten_crystal_matrix).slopeFindDistance(3).levelDecreasePerBlock(3).tickRate(30)
            .block(AIBlocks.molten_crystal_matrix_block).bucket(AIItems.MOLTEN_CRYSTAL_MATRIX_BUCKET);

    public static final DeferredHolder<Fluid,BaseFlowingFluid> source_molten_neutron = REGISTRY.register("molten_neutron",
            ()->new BaseFlowingFluid.Source(AIFluids.molten_neutron));
    public static final DeferredHolder<Fluid,BaseFlowingFluid> flowing_molten_neutron = REGISTRY.register("flowing_molten_neutron",
            ()->new BaseFlowingFluid.Flowing(AIFluids.molten_neutron));
    public static final BaseFlowingFluid.Properties molten_neutron = new BaseFlowingFluid.Properties(
            AIFluidTypes.molten_neutron, source_molten_neutron, flowing_molten_neutron).slopeFindDistance(3).levelDecreasePerBlock(3).tickRate(30)
            .block(AIBlocks.molten_neutron_block).bucket(AIItems.MOLTEN_NEUTRON_BUCKET);

    public static final DeferredHolder<Fluid,BaseFlowingFluid> source_molten_star = REGISTRY.register("molten_star",
            ()->new BaseFlowingFluid.Source(AIFluids.molten_star));
    public static final DeferredHolder<Fluid,BaseFlowingFluid> flowing_molten_star = REGISTRY.register("flowing_molten_star",
            ()->new BaseFlowingFluid.Flowing(AIFluids.molten_star));
    public static final BaseFlowingFluid.Properties molten_star = new BaseFlowingFluid.Properties(
            AIFluidTypes.molten_star, source_molten_star, flowing_molten_star).slopeFindDistance(8).levelDecreasePerBlock(8).tickRate(30)
            .block(AIBlocks.molten_star_block).bucket(AIItems.MOLTEN_STAR_BUCKET);

    public static void registers(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
