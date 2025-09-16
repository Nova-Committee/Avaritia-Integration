package committee.nova.mods.avaritia_integration.init.registry;

import committee.nova.mods.avaritia.api.common.item.BaseItem;
import committee.nova.mods.avaritia.init.registry.ModRarities;
import committee.nova.mods.avaritia_integration.api.module.AbModule;
import committee.nova.module.InModule;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import static committee.nova.mods.avaritia_integration.init.registry.Registries.item;

/**
 * @author: cnlimiter
 */
@InModule(value = "thermal_reg", dependencies = "thermal")
public class ThermalReg extends AbModule {
    public static final RegistryObject<Item> creative_augment_base = item("creative_augment_base", () -> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));

}
