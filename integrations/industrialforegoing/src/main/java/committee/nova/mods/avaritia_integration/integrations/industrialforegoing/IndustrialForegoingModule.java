package committee.nova.mods.avaritia_integration.integrations.industrialforegoing;

import committee.nova.mods.avaritia_integration.integrations.industrialforegoing.item.ModEfficiencyAddonItem;
import committee.nova.mods.avaritia_integration.integrations.industrialforegoing.item.ModProcessingAddonItem;
import committee.nova.mods.avaritia_integration.integrations.industrialforegoing.item.ModSpeedAddonItem;
import committee.nova.mods.avaritia_integration.integrations.industrialforegoing.registry.IndustrialForegoingIntegrationBlocks;
import committee.nova.mods.avaritia_integration.integrations.industrialforegoing.registry.IndustrialForegoingIntegrationFluids;
import committee.nova.mods.avaritia_integration.integrations.industrialforegoing.registry.IndustrialForegoingIntegrationItems;
import committee.nova.mods.avaritia_integration.module.ModMeta;
import committee.nova.mods.avaritia_integration.module.Module;
import committee.nova.mods.avaritia_integration.module.ModuleEntry;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;

import com.buuz135.industrial.item.addon.ProcessingAddonItem;
import com.hrznstudio.titanium.api.augment.AugmentTypes;
import com.hrznstudio.titanium.item.AugmentWrapper;

@ModuleEntry(id = IndustrialForegoingModule.MOD_ID, target = @ModMeta(IndustrialForegoingModule.MOD_ID))
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
            if (obj.get() instanceof ModSpeedAddonItem speedAddonItem) {
                ItemStack stack = new ItemStack(speedAddonItem);
                AugmentWrapper.setType(stack, AugmentTypes.SPEED, (float) (1 + speedAddonItem.getTier()));
                output.accept(stack);
            } else if (obj.get() instanceof ModProcessingAddonItem processingAddonItem) {
                ItemStack stack = new ItemStack(processingAddonItem);
                AugmentWrapper.setType(stack, ProcessingAddonItem.PROCESSING,
                        (float) (1 + processingAddonItem.getTier()));
                output.accept(stack);
            } else if (obj.get() instanceof ModEfficiencyAddonItem efficiencyAddonItem) {
                ItemStack stack = new ItemStack(efficiencyAddonItem);
                AugmentWrapper.setType(stack, AugmentTypes.EFFICIENCY,
                        1.0F - (float) efficiencyAddonItem.getTier() * 0.1F);
                output.accept(stack);
            }
        });
        output.accept(IndustrialForegoingIntegrationFluids.ELDERLY_MEDULLA.getBucketFluid());
        output.accept(IndustrialForegoingIntegrationFluids.VOID_MATTER.getBucketFluid());
    }
}
