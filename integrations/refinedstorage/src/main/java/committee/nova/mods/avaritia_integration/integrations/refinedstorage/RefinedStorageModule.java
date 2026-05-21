package committee.nova.mods.avaritia_integration.integrations.refinedstorage;

import committee.nova.mods.avaritia_integration.integrations.refinedstorage.registry.RefinedStorageIntegrationItems;
import committee.nova.mods.avaritia_integration.module.ModMeta;
import committee.nova.mods.avaritia_integration.module.Module;
import committee.nova.mods.avaritia_integration.module.ModuleEntry;

import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;

@ModuleEntry(id = RefinedStorageModule.MOD_ID, target = @ModMeta(RefinedStorageModule.MOD_ID))
public final class RefinedStorageModule implements Module {

    public static final String MOD_ID = "refinedstorage";

    @Override
    public committee.nova.mods.avaritia_integration.api.load.IntegrationRule defaultLoadRule() {
        return Module.rule(Module.dependency(MOD_ID));
    }

    @Override
    public void init(IEventBus registryBus) {
        RefinedStorageIntegrationItems.REGISTRY.register(registryBus);
    }

    @Override
    public void collectCreativeTabItems(CreativeModeTab.ItemDisplayParameters parameters,
                                        CreativeModeTab.Output output) {
        output.accept(RefinedStorageIntegrationItems.INFINITY_STORAGE_PART.get());
    }
}
