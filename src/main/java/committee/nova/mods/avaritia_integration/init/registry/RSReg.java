package committee.nova.mods.avaritia_integration.init.registry;

import committee.nova.mods.avaritia.api.common.item.BaseItem;
import committee.nova.mods.avaritia.init.registry.ModRarities;
import committee.nova.mods.avaritia_integration.api.module.AbModule;
import committee.nova.mods.avaritia_integration.api.module.InModule;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import static committee.nova.mods.avaritia_integration.init.registry.Registries.item;

/**
 * @author: cnlimiter
 */
@InModule(value = "refinedstorage_reg", dependencies = "refinedstorage")
public class RSReg extends AbModule {
    public static final RegistryObject<Item> infinity_storge_part = item("infinity_storage_part", () -> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
}
