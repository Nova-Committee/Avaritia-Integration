package committee.nova.mods.avaritia_integration.integrations.mysticalagriculture.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class MysticalAgricultureIntegrationItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AvaritiaIntegration.MOD_ID);

    public static final DeferredItem<Item> INFINITY_CRUX = ITEMS.register("infinity_crux",
            () -> new BlockItem(MysticalAgricultureIntegrationBlocks.INFINITY_CRUX.get(), new Item.Properties()));

    public static final DeferredItem<Item> CRYSTAL_MATRIX_CRUX = ITEMS.register("crystal_matrix_crux",
            () -> new BlockItem(MysticalAgricultureIntegrationBlocks.CRYSTAL_MATRIX_CRUX.get(), new Item.Properties()));
}
