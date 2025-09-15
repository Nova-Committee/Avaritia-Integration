package committee.nova.mods.avaritia_integration.module.slashblade;

import committee.nova.mods.avaritia_integration.module.ModMeta;
import committee.nova.mods.avaritia_integration.module.Module;
import committee.nova.mods.avaritia_integration.module.ModuleEntry;
import committee.nova.mods.avaritia_integration.module.slashblade.registry.SlashBladeIntegrationItems;
import net.minecraftforge.eventbus.api.IEventBus;

@ModuleEntry(id = SlashBladeModule.MOD_ID, target = @ModMeta(SlashBladeModule.MOD_ID))
public final class SlashBladeModule implements Module {
    public static final String MOD_ID = "slashblade";

    @Override
    public void init(IEventBus registryBus) {
        SlashBladeIntegrationItems.REGISTRY.register(registryBus);
    }
}
