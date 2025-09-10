package committee.nova.mods.avaritia_integration.init.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;


public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AvaritiaIntegration.MOD_ID);
    public static final List<RegistryObject<Item>> ACCEPT_ITEM = new ArrayList<>();

    public static final RegistryObject<CreativeModeTab> CREATIVE_TAB = CREATIVE_TABS.register("avaritia_integration_group", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.tab.Integration"))
            .icon(()-> ModItems.infinity_gear.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                for (var item : ACCEPT_ITEM) {
                    output.accept(item.get());
                }
            })
            .build());

}
