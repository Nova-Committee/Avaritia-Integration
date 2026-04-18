package committee.nova.mods.avaritia_integration.module.enderio.registry;

import committee.nova.mods.avaritia.api.common.item.BaseItem;
import committee.nova.mods.avaritia.init.registry.ModRarities;
import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.module.enderio.AICapacitorDef;
import committee.nova.mods.avaritia_integration.module.enderio.data.AICapacitorData;
import committee.nova.mods.avaritia_integration.module.enderio.item.AICapacitorItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

public final class EnderIOIntegrationItems {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(Registries.ITEM, AvaritiaIntegration.MOD_ID);

    public static final Map<AICapacitorDef, RegistryObject<Item>> CAPACITORS = new EnumMap<>(AICapacitorDef.class);

    static {
        for (AICapacitorDef def : AICapacitorDef.values()) {
            CAPACITORS.put(def, register(def.id, () -> new AICapacitorItem(
                    new AICapacitorData(def.baseLevel),
                    new Item.Properties().rarity(def.rarity)
            )));
        }
    }

    public static final RegistryObject<Item> INFINITY_GRINDING_BALL = register("infinity_grinding_ball", () -> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    public static final RegistryObject<Item> NEUTRON_GRINDING_BALL = register("neutron_grinding_ball", () -> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));

    public static <T extends Item> RegistryObject<T> register(String id, Supplier<T> obj) {
        return REGISTRY.register(id, obj);
    }
}
