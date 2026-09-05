package committee.nova.mods.avaritia_integration.integrations.botania.botania.registry;

import committee.nova.mods.avaritia.init.registry.ModRarities;
import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.integrations.botania.botania.item.AlphaSparkItem;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class BotaniaIntegrationItems {

    public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(AvaritiaIntegration.MOD_ID);

    public static final DeferredItem<Item> ALPHA_SPARK = register(
            "alpha_spark",
            () -> new AlphaSparkItem(new Item.Properties().rarity(ModRarities.COSMIC.getValue()).fireResistant()));

    public static <T extends Item> DeferredItem<T> register(String id, Supplier<T> obj) {
        return REGISTRY.register(id, obj);
    }
}
