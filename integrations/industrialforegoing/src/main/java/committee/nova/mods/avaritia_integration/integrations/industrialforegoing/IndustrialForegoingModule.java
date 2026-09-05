package committee.nova.mods.avaritia_integration.integrations.industrialforegoing;

import committee.nova.mods.avaritia_integration.integrations.industrialforegoing.item.AddonItem;
import committee.nova.mods.avaritia_integration.integrations.industrialforegoing.registry.IndustrialForegoingIntegrationBlocks;
import committee.nova.mods.avaritia_integration.integrations.industrialforegoing.registry.IndustrialForegoingIntegrationFluids;
import committee.nova.mods.avaritia_integration.integrations.industrialforegoing.registry.IndustrialForegoingIntegrationItems;
import committee.nova.mods.avaritia_integration.module.Module;

import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;

public final class IndustrialForegoingModule implements Module {

    public static final String MOD_ID = "industrialforegoing";

    @Override
    public void init(IEventBus registryBus) {
        IndustrialForegoingIntegrationItems.ITEMS.register(registryBus);
        IndustrialForegoingIntegrationBlocks.BLOCKS.register(registryBus);
        IndustrialForegoingIntegrationFluids.FLUIDS.register(registryBus);
        IndustrialForegoingIntegrationFluids.FLUID_TYPES.register(registryBus);
    }

    @Override
    public void collectCreativeTabItems(CreativeModeTab.ItemDisplayParameters parameters,
                                        CreativeModeTab.Output output) {
        IndustrialForegoingIntegrationItems.ADDONS.forEach((materialName, obj) -> {
            if (obj.get() instanceof AddonItem addon) {
                output.accept(addon.getDefaultInstance());
            }
        });
        output.accept(IndustrialForegoingIntegrationFluids.ELDERLY_MEDULLA.getBucketFluid());
        output.accept(IndustrialForegoingIntegrationFluids.VOID_MATTER.getBucketFluid());
    }
}
