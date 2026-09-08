package committee.nova.mods.avaritia_integration.integrations.mysticalagriculture;

import committee.nova.mods.avaritia_integration.integrations.mysticalagriculture.registry.MysticalAgricultureIntegrationBlocks;
import committee.nova.mods.avaritia_integration.integrations.mysticalagriculture.registry.MysticalAgricultureIntegrationCrops;
import committee.nova.mods.avaritia_integration.integrations.mysticalagriculture.registry.MysticalAgricultureIntegrationItems;
import committee.nova.mods.avaritia_integration.module.Module;

import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;

public final class MysticalAgricultureModule implements Module {

    @Override
    public void init(IEventBus registryBus) {
        MysticalAgricultureIntegrationBlocks.BLOCKS.register(registryBus);
        MysticalAgricultureIntegrationItems.ITEMS.register(registryBus);
    }

    @Override
    public void collectCreativeTabItems(CreativeModeTab.ItemDisplayParameters parameters,
                                        CreativeModeTab.Output output) {
        output.accept(MysticalAgricultureIntegrationCrops.BLAZE_CUBE.getSeedsItem());
        output.accept(MysticalAgricultureIntegrationCrops.BLAZE_CUBE.getEssenceItem());
        output.accept(MysticalAgricultureIntegrationCrops.CRYSTAL_MATRIX.getSeedsItem());
        output.accept(MysticalAgricultureIntegrationCrops.CRYSTAL_MATRIX.getEssenceItem());
        output.accept(MysticalAgricultureIntegrationCrops.INFINITY.getSeedsItem());
        output.accept(MysticalAgricultureIntegrationCrops.INFINITY.getEssenceItem());
        output.accept(MysticalAgricultureIntegrationItems.INFINITY_CRUX.get());
        output.accept(MysticalAgricultureIntegrationItems.CRYSTAL_MATRIX_CRUX.get());
    }
}
