package committee.nova.mods.avaritia_integration.module.enderio;

import committee.nova.mods.avaritia_integration.module.ModMeta;
import committee.nova.mods.avaritia_integration.module.Module;
import committee.nova.mods.avaritia_integration.module.ModuleEntry;
import committee.nova.mods.avaritia_integration.module.enderio.registry.EnderIOIntegrationItems;
import net.minecraftforge.eventbus.api.IEventBus;

@ModuleEntry(id = EnderIOModule.MOD_ID, target = @ModMeta(EnderIOModule.MOD_ID))
public final class EnderIOModule implements Module {
    public static final String MOD_ID = "enderio";

    @Override
    public void init(IEventBus registryBus) {
        EnderIOIntegrationItems.REGISTRY.register(registryBus);
    }
}
