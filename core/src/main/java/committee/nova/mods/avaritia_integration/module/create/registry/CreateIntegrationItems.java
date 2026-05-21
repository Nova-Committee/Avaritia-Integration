package committee.nova.mods.avaritia_integration.module.create.registry;

import committee.nova.mods.avaritia.api.common.item.BaseItem;
import committee.nova.mods.avaritia.init.registry.ModRarities;
import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class CreateIntegrationItems {
    public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(AvaritiaIntegration.MOD_ID);

    public static final DeferredItem<Item> CREATIVE_MECHANISM = register("creative_mechanism", () -> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    public static final DeferredItem<Item> CREATIVE_COMPOUND = register("creative_compound", () -> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));

    public static <T extends Item> DeferredItem<T> register(String id, Supplier<T> obj) {
        return REGISTRY.register(id, obj);
    }
}
