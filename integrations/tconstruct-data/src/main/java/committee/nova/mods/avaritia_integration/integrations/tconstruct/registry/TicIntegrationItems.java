package committee.nova.mods.avaritia_integration.integrations.tconstruct.registry;

import committee.nova.mods.avaritia.init.registry.ModRarities;
import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.integrations.tconstruct.item.HeavenArrowItem;
import committee.nova.mods.avaritia_integration.integrations.tconstruct.item.TraceArrowItem;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class TicIntegrationItems {

    public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(AvaritiaIntegration.MOD_ID);

    public static final DeferredItem<Item> HEAVEN_ARROW = REGISTRY.register("heaven_arrow",
            () -> new HeavenArrowItem(new Item.Properties().rarity(ModRarities.COSMIC.getValue())));

    public static final DeferredItem<Item> TRACE_ARROW = REGISTRY.register("trace_arrow",
            () -> new TraceArrowItem(new Item.Properties().rarity(ModRarities.COSMIC.getValue())));

    public static final DeferredItem<Item> MOLTEN_INFINITY_BUCKET = REGISTRY.register("molten_infinity_bucket",
            () -> new BucketItem(TicIntegrationFluids.SOURCE_MOLTEN_INFINITY.get(),
                    new Item.Properties().stacksTo(1).rarity(ModRarities.COSMIC.getValue())));

    private TicIntegrationItems() {}
}
