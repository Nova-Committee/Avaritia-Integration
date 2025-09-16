package committee.nova.mods.avaritia_integration.module.ae2;

import committee.nova.mods.avaritia_integration.module.ModMeta;
import committee.nova.mods.avaritia_integration.module.Module;
import committee.nova.mods.avaritia_integration.module.ModuleEntry;
import committee.nova.mods.avaritia_integration.module.ae2.registry.AE2IntegrationItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;

@ModuleEntry(id = AE2Module.MOD_ID, target = @ModMeta(AE2Module.MOD_ID))
public final class AE2Module implements Module {
    public static final String MOD_ID = "ae2";

    @Override
    public void init(IEventBus registryBus) {
        AE2IntegrationItems.REGISTRY.register(registryBus);
    }

    @Override
    public void collectCreativeTabItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        output.accept(AE2IntegrationItems.INFINITY_ME_STORAGE_COMPONENT.get());
    }
}
