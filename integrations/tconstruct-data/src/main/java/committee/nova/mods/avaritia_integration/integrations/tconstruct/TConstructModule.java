package committee.nova.mods.avaritia_integration.integrations.tconstruct;

import committee.nova.mods.avaritia_integration.integrations.tconstruct.registry.TicIntegrationBlocks;
import committee.nova.mods.avaritia_integration.integrations.tconstruct.registry.TicIntegrationFluidTypes;
import committee.nova.mods.avaritia_integration.integrations.tconstruct.registry.TicIntegrationFluids;
import committee.nova.mods.avaritia_integration.integrations.tconstruct.registry.TicIntegrationItems;
import committee.nova.mods.avaritia_integration.module.Module;

import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;

public final class TConstructModule implements Module {

    public static final String MOD_ID = "tconstruct";

    @Override
    public void init(IEventBus registryBus) {
        TicIntegrationFluidTypes.REGISTRY.register(registryBus);
        TicIntegrationFluids.REGISTRY.register(registryBus);
        TicIntegrationBlocks.REGISTRY.register(registryBus);
        TicIntegrationItems.REGISTRY.register(registryBus);
    }

    @Override
    public void collectCreativeTabItems(CreativeModeTab.ItemDisplayParameters parameters,
                                        CreativeModeTab.Output output) {
        output.accept(TicIntegrationItems.HEAVEN_ARROW.get());
        output.accept(TicIntegrationItems.TRACE_ARROW.get());
        output.accept(TicIntegrationItems.MOLTEN_INFINITY_BUCKET.get());
    }
}
