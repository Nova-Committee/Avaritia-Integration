package committee.nova.mods.avaritia_integration.module.enderio.registry;

import committee.nova.mods.avaritia.api.common.item.BaseItem;
import committee.nova.mods.avaritia.init.registry.ModRarities;
import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.common.item.misc.InfinityCapacitorData;
import committee.nova.mods.avaritia_integration.module.Shared;
import committee.nova.mods.avaritia_integration.module.enderio.item.InfinityCapacitorItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class EnderIOIntegrationItems {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(Registries.ITEM, AvaritiaIntegration.MOD_ID);

    public static final RegistryObject<Item> INFINITY_CAPACITOR = register("infinity_capacitor", () -> new InfinityCapacitorItem(InfinityCapacitorData.INSTANCE, new Item.Properties().rarity(ModRarities.EPIC)));
    public static final RegistryObject<Item> INFINITY_GRINDING_BALL = register("infinity_grinding_ball", () -> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    public static final RegistryObject<Item> NEUTRON_GRINDING_BALL = register("neutron_grinding_ball", () -> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));

    public static <T extends Item> RegistryObject<T> register(String id, Supplier<T> obj) {
        RegistryObject<T> r = REGISTRY.register(id, obj);
        Shared.appendItemToGroup(r);
        return r;
    }
}
