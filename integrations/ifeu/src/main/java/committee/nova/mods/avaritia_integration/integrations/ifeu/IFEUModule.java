package committee.nova.mods.avaritia_integration.integrations.ifeu;

import committee.nova.mods.avaritia_integration.integrations.ifeu.registry.IFEUIntegrationItems;
import committee.nova.mods.avaritia_integration.module.Module;

import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;

public class IFEUModule implements Module {

    public static final String MOD_ID = "ifeu";

    @Override
    public void init(IEventBus registryBus) {
        IFEUIntegrationItems.ITEMS.register(registryBus);
    }

    @Override
    public void collectCreativeTabItems(CreativeModeTab.ItemDisplayParameters parameters,
                                        CreativeModeTab.Output output) {
        IFEUIntegrationItems.ITEMS.getEntries().forEach(itemDeferredHolder -> {
            output.accept(itemDeferredHolder.get());
        });
    }
}
