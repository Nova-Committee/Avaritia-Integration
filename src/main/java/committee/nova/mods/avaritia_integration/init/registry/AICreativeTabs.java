package committee.nova.mods.avaritia_integration.init.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.module.ModuleManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public final class AICreativeTabs {
    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB, AvaritiaIntegration.MOD_ID);

    public static final RegistryObject<CreativeModeTab> CREATIVE_TAB = register("avaritia_integration_group", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.tab.Integration"))
            .icon(() -> ModItems.infinity_gear.get().getDefaultInstance())
            .displayItems(ModuleManager::collectCreativeTabItems)
            .build());

    private static <T extends CreativeModeTab> RegistryObject<T> register(String id, Supplier<T> obj) {
        return REGISTRY.register(id, obj);
    }
}
