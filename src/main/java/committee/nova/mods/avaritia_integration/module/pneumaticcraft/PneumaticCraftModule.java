package committee.nova.mods.avaritia_integration.module.pneumaticcraft;

import committee.nova.mods.avaritia_integration.module.ModMeta;
import committee.nova.mods.avaritia_integration.module.Module;
import committee.nova.mods.avaritia_integration.module.ModuleEntry;
import committee.nova.mods.avaritia_integration.module.pneumaticcraft.registry.PneumaticCraftIntegrationItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;

@ModuleEntry(id = PneumaticCraftModule.MOD_ID, target = @ModMeta(PneumaticCraftModule.MOD_ID))
public final class PneumaticCraftModule implements Module {
    public static final String MOD_ID = "pneumaticcraft";

    @Override
    public void init(IEventBus registryBus) {
        PneumaticCraftIntegrationItems.REGISTRY.register(registryBus);
    }

    @Override
    public void collectCreativeTabItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        output.accept(PneumaticCraftIntegrationItems.CREATIVE_COMPRESSED_IRON.get());
    }
}
