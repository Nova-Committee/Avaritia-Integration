package committee.nova.mods.avaritia_integration.init.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.module.ModuleManager;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@SuppressWarnings("unused")
public final class AICreativeTabs {

    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister
            .create(Registries.CREATIVE_MODE_TAB, AvaritiaIntegration.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_TAB = REGISTRY
            .register("avaritia_integration_group", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.tab.Integration"))
                    .icon(() -> AIItems.INFINITY_GEAR.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        AIItems.ITEMS.forEach(r -> output.accept(r.get()));
                        ModuleManager.collectCreativeTabItems(parameters, output);
                    })
                    .build());

    public static void register(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
