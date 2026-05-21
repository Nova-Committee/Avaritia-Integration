package committee.nova.mods.avaritia_integration.init.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.joml.Vector3f;

public class AIFluidTypes {

    public static final DeferredRegister<FluidType> REGISTRY =
            DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, AvaritiaIntegration.MOD_ID);


    public static final DeferredHolder<FluidType, FluidType> molten_blaze = REGISTRY.register("molten_blaze",
            ()->new BaseFluidType("molten_blaze", new Vector3f(253f/225f,192f/225f,69f/255f),
                    base("molten_blaze").temperature(3300).lightLevel(15)));

    public static final DeferredHolder<FluidType, FluidType> molten_crystal_matrix = REGISTRY.register("molten_crystal_matrix",
            ()->new BaseFluidType("molten_crystal_matrix", new Vector3f(103f/225f,187f/225f,182f/255f),
                    base("molten_crystal_matrix").temperature(2800).lightLevel(15)));

    public static final DeferredHolder<FluidType, FluidType> molten_neutron = REGISTRY.register("molten_neutron",
            ()->new BaseFluidType("molten_neutron", new Vector3f(160f/225f,164f/225f,191f/255f),
                    base("molten_neutron").temperature(8300).lightLevel(15)));


    public static final DeferredHolder<FluidType, FluidType> molten_star = REGISTRY.register("molten_star",
            ()->new BaseFluidType("molten_star", new Vector3f(94f/225f,157f/225f,221f/255f),
                    base("molten_star").temperature(12300).lightLevel(15)));

    private static FluidType.Properties base(String name) {
        return FluidType.Properties.create().viscosity(10000).density(2000).descriptionId("fluid.avaritia_integration." + name).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).
                sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY).canSwim(false).canDrown(false).pathType(PathType.LAVA).adjacentPathType(null)
                .motionScale(0.0023333333333333335);
    }

    public static void registers(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
