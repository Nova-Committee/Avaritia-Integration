package committee.nova.mods.avaritia_integration.integrations.create.registry;

import committee.nova.mods.avaritia.api.common.item.BaseItem;
import committee.nova.mods.avaritia.init.registry.ModRarities;
import committee.nova.mods.avaritia_integration.AvaritiaIntegration;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public final class CreateIntegrationItems {

    public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(AvaritiaIntegration.MOD_ID);

    public static final DeferredItem<Item> CREATIVE_MECHANISM = register("creative_mechanism",
            () -> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    public static final DeferredItem<Item> CREATIVE_COMPOUND = register("creative_compound",
            () -> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    public static final DeferredItem<Item> STAR_CAKE_BASE = register("star_cake_base",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> STAR_CAKE = register("star_cake",
            () -> new FuelItem(new Item.Properties(), Integer.MAX_VALUE));
    public static final DeferredItem<Item> IGNIS_CAKE_BASE = register("ignis_cake_base",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> IGNIS_CAKE = register("ignis_cake",
            () -> new FuelItem(new Item.Properties(), 65536));

    public static <T extends Item> DeferredItem<T> register(String id, Supplier<T> obj) {
        return REGISTRY.register(id, obj);
    }

    public static final class FuelItem extends Item {
        private final int burnTime;

        public FuelItem(Properties properties, int burnTime) {
            super(properties);
            this.burnTime = burnTime;
        }

        @Override
        public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
            return burnTime;
        }
    }
}
