package committee.nova.mods.avaritia_integration.module.ae2.registry;

import committee.nova.mods.avaritia.api.common.item.BaseItem;
import committee.nova.mods.avaritia.init.registry.ModRarities;
import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.module.ae2.AE2Module;
import committee.nova.mods.avaritia_integration.module.ae2.item.InfiniteCellItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public final class AE2IntegrationItems {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(Registries.ITEM, AvaritiaIntegration.MOD_ID);

    public static final RegistryObject<Item> INFINITY_ME_STORAGE_COMPONENT = register("infinity_me_storage_component", () -> new BaseItem(p -> p.rarity(ModRarities.EPIC)));

    public static final RegistryObject<Item> INFINITY_ME_STORAGE_CELL = register("infinity_me_storage_cell",
            () -> {
                if(ModList.get().isLoaded(AE2Module.MOD_ID))
                    return new InfiniteCellItem(new Item.Properties().stacksTo(1), 8);
                else
                    return new BaseItem(p -> p.rarity(ModRarities.EPIC));
            });

    public static <T extends Item> RegistryObject<T> register(String id, Supplier<T> obj) {
        return REGISTRY.register(id, obj);
    }
}
