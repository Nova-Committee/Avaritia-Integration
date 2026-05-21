package committee.nova.mods.avaritia_integration.integrations.enderio.registry;

import committee.nova.mods.avaritia.api.common.item.BaseItem;
import committee.nova.mods.avaritia.init.registry.ModRarities;
import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.integrations.enderio.item.InfinityCapacitorItem;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import com.enderio.enderio.api.capacitor.CapacitorData;
import com.enderio.enderio.init.EIODataComponents;

import java.util.function.Supplier;

public final class EnderIOIntegrationItems {

    public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(AvaritiaIntegration.MOD_ID);

    public static final DeferredItem<Item> INFINITY_CAPACITOR = register("infinity_capacitor",
            () -> new InfinityCapacitorItem(new Item.Properties().rarity(ModRarities.EPIC)
                    .component(EIODataComponents.CAPACITOR_DATA, CapacitorData.simple(9.999999046325684F))));
    public static final DeferredItem<Item> INFINITY_GRINDING_BALL = register("infinity_grinding_ball",
            () -> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    public static final DeferredItem<Item> NEUTRON_GRINDING_BALL = register("neutron_grinding_ball",
            () -> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));

    public static <T extends Item> DeferredItem<T> register(String id, Supplier<T> obj) {
        return REGISTRY.register(id, obj);
    }
}
