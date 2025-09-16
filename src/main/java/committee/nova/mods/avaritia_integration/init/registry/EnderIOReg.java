package committee.nova.mods.avaritia_integration.init.registry;

import committee.nova.mods.avaritia.api.common.item.BaseItem;
import committee.nova.mods.avaritia.init.registry.ModRarities;
import committee.nova.mods.avaritia_integration.api.module.AbModule;
import committee.nova.module.InModule;
import committee.nova.mods.avaritia_integration.common.item.InfinityCapacitorItem;
import committee.nova.mods.avaritia_integration.common.item.misc.InfinityCapacitorData;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import static committee.nova.mods.avaritia_integration.init.registry.Registries.item;

/**
 * @author: cnlimiter
 */
@InModule(value = "enderio_reg", dependencies = "enderio")
public class EnderIOReg extends AbModule {
    public static final RegistryObject<Item> infinity_capacitor = item("infinity_capacitor", () -> new InfinityCapacitorItem(InfinityCapacitorData.INSTANCE, new Item.Properties().rarity(ModRarities.EPIC)));
    public static final RegistryObject<Item> infinity_grinding_ball = item("infinity_grinding_ball", () -> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    public static final RegistryObject<Item> neutron_grinding_ball = item("neutron_grinding_ball", () -> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
}
