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
@InModule(value = "ae2_reg", dependencies = "ae2")
public class AE2Reg extends AbModule {
    public static final RegistryObject<Item> infinity_me_storage_component = item("infinity_me_storage_component", () -> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));

}
