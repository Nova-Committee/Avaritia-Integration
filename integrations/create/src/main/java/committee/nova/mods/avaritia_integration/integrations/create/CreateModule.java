package committee.nova.mods.avaritia_integration.integrations.create;

import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationItems;
import committee.nova.mods.avaritia_integration.module.Module;

import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;

public final class CreateModule implements Module {

    public static final String MOD_ID = "create";

    @Override
    public void init(IEventBus registryBus) {
        CreateIntegrationItems.REGISTRY.register(registryBus);
    }

    @Override
    public void collectCreativeTabItems(CreativeModeTab.ItemDisplayParameters parameters,
                                        CreativeModeTab.Output output) {
        output.accept(CreateIntegrationItems.CREATIVE_MECHANISM.get());
        output.accept(CreateIntegrationItems.CREATIVE_COMPOUND.get());
    }
}
