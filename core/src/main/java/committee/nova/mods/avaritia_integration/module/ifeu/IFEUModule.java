package committee.nova.mods.avaritia_integration.module.ifeu;

import committee.nova.mods.avaritia_integration.module.ModMeta;
import committee.nova.mods.avaritia_integration.module.Module;
import committee.nova.mods.avaritia_integration.module.ModuleEntry;
import committee.nova.mods.avaritia_integration.module.ifeu.rregistry.IFEUIntegrationItems;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;

@ModuleEntry(id = IFEUModule.MOD_ID, target = @ModMeta(IFEUModule.MOD_ID))
public class IFEUModule implements Module {
    public static final String MOD_ID = "ifeu";

    @Override
    public void init(IEventBus registryBus) {
        IFEUIntegrationItems.ITEMS.register(registryBus);
    }


    @Override
    public void collectCreativeTabItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        IFEUIntegrationItems.ITEMS.getEntries().forEach(itemDeferredHolder -> {
            output.accept(itemDeferredHolder.get());
        });
    }
}
