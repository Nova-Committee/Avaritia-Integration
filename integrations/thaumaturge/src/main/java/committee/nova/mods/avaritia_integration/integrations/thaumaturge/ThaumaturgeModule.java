package committee.nova.mods.avaritia_integration.integrations.thaumaturge;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.module.Module;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ThaumaturgeModule implements Module {

    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AvaritiaIntegration.MOD_ID);
    private static final DeferredItem<Item> EXTREMELY_PRIMORDIAL_PEARL = ITEMS
            .registerSimpleItem("extremely_primordial_pearl");

    @Override
    public void init(IEventBus registryBus) {
        ITEMS.register(registryBus);
    }

    @Override
    public void collectCreativeTabItems(CreativeModeTab.ItemDisplayParameters parameters,
                                        CreativeModeTab.Output output) {
        output.accept(EXTREMELY_PRIMORDIAL_PEARL.get());
    }
}
