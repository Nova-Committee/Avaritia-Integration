package committee.nova.mods.avaritia_integration.integrations.pneumaticcraft;

import committee.nova.mods.avaritia_integration.integrations.pneumaticcraft.registry.PneumaticCraftIntegrationItems;
import committee.nova.mods.avaritia_integration.module.Module;

import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;

public final class PneumaticCraftModule implements Module {

    public static final String MOD_ID = "pneumaticcraft";

    @Override
    public void init(IEventBus registryBus) {
        PneumaticCraftIntegrationItems.REGISTRY.register(registryBus);
    }

    @Override
    public void collectCreativeTabItems(CreativeModeTab.ItemDisplayParameters parameters,
                                        CreativeModeTab.Output output) {
        output.accept(PneumaticCraftIntegrationItems.CREATIVE_COMPRESSED_IRON.get());
    }
}
