package committee.nova.mods.avaritia_integration.module.industrialforegoing.registry;

import com.hrznstudio.titanium.fluid.ClientFluidTypeExtensions;
import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.module.industrialforegoing.fluid.IFBaseFluidInstance;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class IndustrialForegoingIntegrationFluids {

    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(Registries.FLUID, AvaritiaIntegration.MOD_ID);

    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, AvaritiaIntegration.MOD_ID);

    public static final IFBaseFluidInstance ELDERLY_MEDULLA = register("elderly_medulla");
    public static final IFBaseFluidInstance VOID_MATTER = register("void_matter");

    private static IFBaseFluidInstance register(String name){
        return new IFBaseFluidInstance(
                IndustrialForegoingIntegrationItems.ITEMS,
                IndustrialForegoingIntegrationBlocks.BLOCKS,
                FLUIDS, FLUID_TYPES,
                name,
                FluidType.Properties.create().density(1000),
                new ClientFluidTypeExtensions(
                        AvaritiaIntegration.rl("block/fluids/" + name + "_still"),
                        AvaritiaIntegration.rl("block/fluids/" + name + "_flow")
                )
        );
    }
}