package committee.nova.mods.avaritia_integration.module.ae2.registry;

import committee.nova.mods.avaritia.api.common.item.BaseItem;
import committee.nova.mods.avaritia.init.registry.ModRarities;
import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.module.ae2.AE2Module;
import committee.nova.mods.avaritia_integration.module.ae2.item.AEBigIntegerCellItem;
import committee.nova.mods.avaritia_integration.module.ae2.item.InfiniteCellItem;
import net.minecraft.world.item.Item;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class AE2IntegrationItems {
    public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(AvaritiaIntegration.MOD_ID);

    public static final DeferredItem<Item> INFINITY_ME_STORAGE_COMPONENT = register("infinity_me_storage_component", () -> new BaseItem(p -> p.rarity(ModRarities.EPIC)));

    public static final DeferredItem<Item> INFINITY_ME_STORAGE_CELL = register("infinity_me_storage_cell",
            () -> {
                if (ModList.get().isLoaded(AE2Module.MOD_ID))
                    return new InfiniteCellItem(new Item.Properties().stacksTo(1).rarity(ModRarities.EPIC), 8);
                else
                    return new BaseItem(p -> p.rarity(ModRarities.EPIC));
            });

    public static final DeferredItem<Item> INFINITY_ME_STORAGE_CELL_BIG = register("infinity_me_storage_cell_big",
            () -> {
                if (ModList.get().isLoaded(AE2Module.MOD_ID))
                    return new AEBigIntegerCellItem(new Item.Properties().stacksTo(1).rarity(ModRarities.EPIC), 64);
                else
                    return new BaseItem(p -> p.rarity(ModRarities.EPIC));
            });

    public static <T extends Item> DeferredItem<T> register(String id, Supplier<T> obj) {
        return REGISTRY.register(id, obj);
    }
}
