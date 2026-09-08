package committee.nova.mods.avaritia_integration.integrations.tconstruct.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.init.registry.BaseFluidType;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import org.joml.Vector3f;

public final class TicIntegrationFluidTypes {

    public static final DeferredRegister<FluidType> REGISTRY = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES,
            AvaritiaIntegration.MOD_ID);

    public static final DeferredHolder<FluidType, FluidType> MOLTEN_INFINITY = REGISTRY.register("molten_infinity",
            () -> new BaseFluidType("molten_infinity", new Vector3f(1f, 1f, 1f),
                    FluidType.Properties.create().viscosity(10000).density(-2000)
                            .descriptionId("fluid.avaritia_integration.molten_infinity")
                            .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
                            .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
                            .canSwim(false).canDrown(false).pathType(PathType.LAVA).adjacentPathType(null)
                            .motionScale(0.0023333333333333335).temperature(12300).lightLevel(15)));

    private TicIntegrationFluidTypes() {}
}
