package committee.nova.mods.avaritia_integration.init.registry;

import committee.nova.mods.avaritia.api.common.item.BaseItem;
import committee.nova.mods.avaritia.init.registry.ModRarities;
import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class AIItems {
    public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(AvaritiaIntegration.MOD_ID);

    public static final List<DeferredItem<? extends Item>> ITEMS = new LinkedList<>();
    //blaze_cube
    public static final DeferredItem<Item> BLAZE_CUBE_BOLT = register("blaze_cube_bolt", () -> new BaseItem(pro -> pro.rarity(ModRarities.UNCOMMON)));
    public static final DeferredItem<Item> BLAZE_CUBE_DENSE_PLATE = register("blaze_cube_dense_plate", () -> new BaseItem(pro -> pro.rarity(ModRarities.UNCOMMON)));
    public static final DeferredItem<Item> BLAZE_CUBE_DOUBLE_PLATE = register("blaze_cube_double_plate", () -> new BaseItem(pro -> pro.rarity(ModRarities.UNCOMMON)));
    public static final DeferredItem<Item> BLAZE_CUBE_DUST = register("blaze_cube_dust", () -> new BaseItem(pro -> pro.rarity(ModRarities.UNCOMMON)));
    public static final DeferredItem<Item> BLAZE_CUBE_GEAR = register("blaze_cube_gear", () -> new BaseItem(pro -> pro.rarity(ModRarities.UNCOMMON)));
    public static final DeferredItem<Item> BLAZE_CUBE_LONG_ROD = register("blaze_cube_long_rod", () -> new BaseItem(pro -> pro.rarity(ModRarities.UNCOMMON)));
    public static final DeferredItem<Item> BLAZE_CUBE_NUGGET = register("blaze_cube_nugget", () -> new BaseItem(pro -> pro.rarity(ModRarities.UNCOMMON)));
    public static final DeferredItem<Item> BLAZE_CUBE_PLATE = register("blaze_cube_plate", () -> new BaseItem(pro -> pro.rarity(ModRarities.UNCOMMON)));
    public static final DeferredItem<Item> BLAZE_CUBE_RING = register("blaze_cube_ring", () -> new BaseItem(pro -> pro.rarity(ModRarities.UNCOMMON)));
    public static final DeferredItem<Item> BLAZE_CUBE_ROD = register("blaze_cube_rod", () -> new BaseItem(pro -> pro.rarity(ModRarities.UNCOMMON)));
    public static final DeferredItem<Item> BLAZE_CUBE_SCREW = register("blaze_cube_screw", () -> new BaseItem(pro -> pro.rarity(ModRarities.UNCOMMON)));
    public static final DeferredItem<Item> BLAZE_CUBE_SPRING = register("blaze_cube_spring", () -> new BaseItem(pro -> pro.rarity(ModRarities.UNCOMMON)));
    public static final DeferredItem<Item> BLAZE_CUBE_WIRE = register("blaze_cube_wire", () -> new BaseItem(pro -> pro.rarity(ModRarities.UNCOMMON)));
    public static final DeferredItem<Item> MOLTEN_BLAZE_BUCKET = register("molten_blaze_bucket", () -> new BucketItem(AIFluids.source_molten_blaze.get(), new Item.Properties().stacksTo(1).rarity(ModRarities.UNCOMMON)));
    //crystal_matrix
    public static final DeferredItem<Item> CRYSTAL_MATRIX_BOLT = register("crystal_matrix_bolt", () -> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final DeferredItem<Item> CRYSTAL_MATRIX_DENSE_PLATE = register("crystal_matrix_dense_plate", () -> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final DeferredItem<Item> CRYSTAL_MATRIX_DOUBLE_PLATE = register("crystal_matrix_double_plate", () -> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final DeferredItem<Item> CRYSTAL_MATRIX_DUST = register("crystal_matrix_dust", () -> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final DeferredItem<Item> CRYSTAL_MATRIX_GEAR = register("crystal_matrix_gear", () -> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final DeferredItem<Item> CRYSTAL_MATRIX_LONG_ROD = register("crystal_matrix_long_rod", () -> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final DeferredItem<Item> CRYSTAL_MATRIX_NUGGET = register("crystal_matrix_nugget", () -> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final DeferredItem<Item> CRYSTAL_MATRIX_PLATE = register("crystal_matrix_plate", () -> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final DeferredItem<Item> CRYSTAL_MATRIX_RING = register("crystal_matrix_ring", () -> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final DeferredItem<Item> CRYSTAL_MATRIX_ROD = register("crystal_matrix_rod", () -> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final DeferredItem<Item> CRYSTAL_MATRIX_SCREW = register("crystal_matrix_screw", () -> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final DeferredItem<Item> CRYSTAL_MATRIX_SPRING = register("crystal_matrix_spring", () -> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final DeferredItem<Item> CRYSTAL_MATRIX_WIRE = register("crystal_matrix_wire", () -> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final DeferredItem<Item> MOLTEN_CRYSTAL_MATRIX_BUCKET = register("molten_crystal_matrix_bucket", () -> new BucketItem(AIFluids.source_molten_crystal_matrix.get(), new Item.Properties().stacksTo(1).rarity(ModRarities.RARE)));
    //infinity
    public static final DeferredItem<Item> INFINITY_BOLT = register("infinity_bolt", () -> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    public static final DeferredItem<Item> INFINITY_DENSE_PLATE = register("infinity_dense_plate", () -> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    public static final DeferredItem<Item> INFINITY_DOUBLE_PLATE = register("infinity_double_plate", () -> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    public static final DeferredItem<Item> INFINITY_DUST = register("infinity_dust", () -> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    public static final DeferredItem<Item> INFINITY_GEAR = register("infinity_gear", () -> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    public static final DeferredItem<Item> INFINITY_LONG_ROD = register("infinity_long_rod", () -> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    public static final DeferredItem<Item> INFINITY_PLATE = register("infinity_plate", () -> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    public static final DeferredItem<Item> INFINITY_RING = register("infinity_ring", () -> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    public static final DeferredItem<Item> INFINITY_ROD = register("infinity_rod", () -> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    public static final DeferredItem<Item> INFINITY_SCREW = register("infinity_screw", () -> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    public static final DeferredItem<Item> INFINITY_SPRING = register("infinity_spring", () -> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    public static final DeferredItem<Item> INFINITY_WIRE = register("infinity_wire", () -> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    //neutron
    public static final DeferredItem<Item> NEUTRON_BOLT = register("neutron_bolt", () -> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final DeferredItem<Item> NEUTRON_DENSE_PLATE = register("neutron_dense_plate", () -> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final DeferredItem<Item> NEUTRON_DOUBLE_PLATE = register("neutron_double_plate", () -> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final DeferredItem<Item> NEUTRON_DUST = register("neutron_dust", () -> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final DeferredItem<Item> NEUTRON_LONG_ROD = register("neutron_long_rod", () -> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final DeferredItem<Item> NEUTRON_PLATE = register("neutron_plate", () -> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final DeferredItem<Item> NEUTRON_RING = register("neutron_ring", () -> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final DeferredItem<Item> NEUTRON_ROD = register("neutron_rod", () -> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final DeferredItem<Item> NEUTRON_SCREW = register("neutron_screw", () -> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final DeferredItem<Item> NEUTRON_SPRING = register("neutron_spring", () -> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final DeferredItem<Item> NEUTRON_WIRE = register("neutron_wire", () -> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final DeferredItem<Item> MOLTEN_NEUTRON_BUCKET = register("molten_neutron_bucket", () -> new BucketItem(AIFluids.source_molten_neutron.get(), new Item.Properties().stacksTo(1).rarity(ModRarities.RARE)));

    public static final DeferredItem<Item> MOLTEN_STAR_BUCKET = register("molten_star_bucket", () -> new BucketItem(AIFluids.source_molten_star.get(), new Item.Properties().stacksTo(1).rarity(ModRarities.RARE)));

    public static <T extends Item> DeferredItem<T> register(String id, Supplier<T> obj) {
        DeferredItem<T> r = REGISTRY.register(id, obj);
        ITEMS.add(r);
        return r;
    }
}
