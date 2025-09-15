package committee.nova.mods.avaritia_integration.init.registry;

import committee.nova.mods.avaritia.api.common.item.BaseItem;
import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author: cnlimiter
 */
public class Registries {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AvaritiaIntegration.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AvaritiaIntegration.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB, AvaritiaIntegration.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, AvaritiaIntegration.MOD_ID);

    public static final List<RegistryObject<Item>> ACCEPT_ITEM = new ArrayList<>();

    public static final RegistryObject<CreativeModeTab> CREATIVE_TAB = CREATIVE_TABS.register("avaritia_integration_group", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.tab.Integration"))
            //.icon(()-> committee.nova.mods.avaritia_integration.init.registry.ModItems.infinity_gear.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                for (var item : ACCEPT_ITEM) {
                    output.accept(item.get());
                }
            })
            .build());


    public static void init(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        CREATIVE_TABS.register(bus);
        BLOCK_ENTITIES.register(bus);
    }

    private static RegistryObject<Block> baseBlock(String name, Supplier<Block> block) {
        return BLOCKS.register(name, block);
    }


    public static RegistryObject<Block> itemBlock(String name, Supplier<Block> block) {
        return itemBlock(name, block, true);
    }

    public static RegistryObject<Block> itemBlock(String name, Supplier<Block> block, boolean hasItem) {
        return itemBlock(name, block, hasItem, true, new Item.Properties());
    }

    public static RegistryObject<Block> itemBlock(String name, Supplier<Block> block, boolean hasItem, boolean exist) {
        return itemBlock(name, block, hasItem, exist, new Item.Properties());
    }

    public static RegistryObject<Block> itemBlock(String name, Supplier<Block> block, Rarity rarity) {
        return itemBlock(name, block, true, true, new Item.Properties().rarity(rarity));
    }

    public static RegistryObject<Block> itemBlock(String name, Supplier<Block> block,  boolean hasItem, Item.Properties properties) {
        return itemBlock(name, block, hasItem, true, properties);
    }

    public static RegistryObject<Block> itemBlock(String name, Supplier<Block> block, boolean hasItem, boolean exist, Item.Properties properties) {
        var reg = BLOCKS.register(name, block);
        if (hasItem) item(name, () -> new BlockItem(reg.get(), properties), exist);
        return reg;
    }
    public static RegistryObject<Item> item(String name) {
        return item(name, true);
    }

    public static RegistryObject<Item> item(String name, boolean exist) {
        return item(name, (e) -> new BaseItem(), exist);
    }

    public static RegistryObject<Item> item(String name, Function<String, Item> item) {
        return item(name, item, true);
    }

    public static RegistryObject<Item> item(String name, Function<String, Item> item, boolean exist) {
        return item(name, () -> item.apply(name), exist);
    }

    public static RegistryObject<Item> item(String name, Supplier<Item> item) {
        return item(name, item, true);
    }

    public static RegistryObject<Item> item(String name, Supplier<Item> item, boolean exist) {
        var regItem = ITEMS.register(name, item);
        if (exist) ACCEPT_ITEM.add(regItem);
        return regItem;
    }

}
