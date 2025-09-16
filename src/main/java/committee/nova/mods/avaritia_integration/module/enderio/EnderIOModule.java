package committee.nova.mods.avaritia_integration.module.enderio;

import committee.nova.mods.avaritia_integration.module.ModMeta;
import committee.nova.mods.avaritia_integration.module.Module;
import committee.nova.mods.avaritia_integration.module.ModuleEntry;
import committee.nova.mods.avaritia_integration.module.enderio.registry.EnderIOIntegrationItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;

@ModuleEntry(id = EnderIOModule.MOD_ID, target = @ModMeta(EnderIOModule.MOD_ID))
public final class EnderIOModule implements Module {
    public static final String MOD_ID = "enderio";

    @Override
    public void init(IEventBus registryBus) {
        EnderIOIntegrationItems.REGISTRY.register(registryBus);
    }

    @Override
    public void collectCreativeTabItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        output.accept(EnderIOIntegrationItems.INFINITY_CAPACITOR.get());
        output.accept(EnderIOIntegrationItems.INFINITY_GRINDING_BALL.get());
        output.accept(EnderIOIntegrationItems.NEUTRON_GRINDING_BALL.get());
    }
}
