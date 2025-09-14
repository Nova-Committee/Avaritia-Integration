package committee.nova.mods.avaritia_integration.init.registry;

import committee.nova.mods.avaritia.api.common.item.BaseItem;
import committee.nova.mods.avaritia.init.registry.ModRarities;
import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.common.item.*;
import committee.nova.mods.avaritia_integration.common.item.misc.InfinityCapacitorData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import wayoftime.bloodmagic.common.item.BloodOrb;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AvaritiaIntegration.MOD_ID);

    //blaze_cube
    public static final RegistryObject<Item> blaze_cube_bolt = item("blaze_cube_bolt", ()-> new BaseItem(pro -> pro.rarity(ModRarities.UNCOMMON)));
    public static final RegistryObject<Item> blaze_cube_dense_plate = item("blaze_cube_dense_plate", ()-> new BaseItem(pro -> pro.rarity(ModRarities.UNCOMMON)));
    public static final RegistryObject<Item> blaze_cube_double_plate = item("blaze_cube_double_plate", ()-> new BaseItem(pro -> pro.rarity(ModRarities.UNCOMMON)));
    public static final RegistryObject<Item> blaze_cube_dust = item("blaze_cube_dust",()-> new BaseItem(pro -> pro.rarity(ModRarities.UNCOMMON)));
    public static final RegistryObject<Item> blaze_cube_gear = item("blaze_cube_gear", ()-> new BaseItem(pro -> pro.rarity(ModRarities.UNCOMMON)));
    public static final RegistryObject<Item> blaze_cube_long_rod = item("blaze_cube_long_rod", ()-> new BaseItem(pro -> pro.rarity(ModRarities.UNCOMMON)));
    public static final RegistryObject<Item> blaze_cube_nugget = item("blaze_cube_nugget",()-> new BaseItem(pro -> pro.rarity(ModRarities.UNCOMMON)));
    public static final RegistryObject<Item> blaze_cube_plate = item("blaze_cube_plate",()-> new BaseItem(pro -> pro.rarity(ModRarities.UNCOMMON)));
    public static final RegistryObject<Item> blaze_cube_ring = item("blaze_cube_ring", ()-> new BaseItem(pro -> pro.rarity(ModRarities.UNCOMMON)));
    public static final RegistryObject<Item> blaze_cube_rod = item("blaze_cube_rod", ()-> new BaseItem(pro -> pro.rarity(ModRarities.UNCOMMON)));
    public static final RegistryObject<Item> blaze_cube_screw = item("blaze_cube_screw", ()-> new BaseItem(pro -> pro.rarity(ModRarities.UNCOMMON)));
    public static final RegistryObject<Item> blaze_cube_spring = item("blaze_cube_spring", ()-> new BaseItem(pro -> pro.rarity(ModRarities.UNCOMMON)));
    public static final RegistryObject<Item> blaze_cube_wire = item("blaze_cube_wire", ()-> new BaseItem(pro -> pro.rarity(ModRarities.UNCOMMON)));
    //crystal_matrix
    public static final RegistryObject<Item> crystal_matrix_bolt = item("crystal_matrix_bolt", ()-> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final RegistryObject<Item> crystal_matrix_dense_plate = item("crystal_matrix_dense_plate", ()-> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final RegistryObject<Item> crystal_matrix_double_plate = item("crystal_matrix_double_plate", ()-> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final RegistryObject<Item> crystal_matrix_dust = item("crystal_matrix_dust",()-> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final RegistryObject<Item> crystal_matrix_gear = item("crystal_matrix_gear", ()-> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final RegistryObject<Item> crystal_matrix_long_rod = item("crystal_matrix_long_rod", ()-> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final RegistryObject<Item> crystal_matrix_nugget = item("crystal_matrix_nugget",()-> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final RegistryObject<Item> crystal_matrix_plate = item("crystal_matrix_plate",()-> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final RegistryObject<Item> crystal_matrix_ring = item("crystal_matrix_ring", ()-> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final RegistryObject<Item> crystal_matrix_rod = item("crystal_matrix_rod", ()-> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final RegistryObject<Item> crystal_matrix_screw = item("crystal_matrix_screw", ()-> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final RegistryObject<Item> crystal_matrix_spring = item("crystal_matrix_spring", ()-> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final RegistryObject<Item> crystal_matrix_wire = item("crystal_matrix_wire", ()-> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    //infinity
    public static final RegistryObject<Item> infinity_bolt = item("infinity_bolt", ()-> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    public static final RegistryObject<Item> infinity_dense_plate = item("infinity_dense_plate", ()-> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    public static final RegistryObject<Item> infinity_double_plate = item("infinity_double_plate", ()-> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    public static final RegistryObject<Item> infinity_dust = item("infinity_dust",()-> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    public static final RegistryObject<Item> infinity_gear = item("infinity_gear", ()-> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    public static final RegistryObject<Item> infinity_long_rod = item("infinity_long_rod", ()-> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    public static final RegistryObject<Item> infinity_plate = item("infinity_plate",()-> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    public static final RegistryObject<Item> infinity_ring = item("infinity_ring", ()-> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    public static final RegistryObject<Item> infinity_rod = item("infinity_rod", ()-> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    public static final RegistryObject<Item> infinity_screw = item("infinity_screw", ()-> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    public static final RegistryObject<Item> infinity_spring = item("infinity_spring", ()-> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    public static final RegistryObject<Item> infinity_wire = item("infinity_wire", ()-> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    //neutron
    public static final RegistryObject<Item> neutron_bolt = item("neutron_bolt", ()-> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final RegistryObject<Item> neutron_dense_plate = item("neutron_dense_plate", ()-> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final RegistryObject<Item> neutron_double_plate = item("neutron_double_plate", ()-> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final RegistryObject<Item> neutron_dust = item("neutron_dust",()-> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final RegistryObject<Item> neutron_long_rod = item("neutron_long_rod", ()-> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final RegistryObject<Item> neutron_plate = item("neutron_plate",()-> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final RegistryObject<Item> neutron_ring = item("neutron_ring", ()-> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final RegistryObject<Item> neutron_rod = item("neutron_rod", ()-> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final RegistryObject<Item> neutron_screw = item("neutron_screw", ()-> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final RegistryObject<Item> neutron_spring = item("neutron_spring", ()-> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
    public static final RegistryObject<Item> neutron_wire = item("neutron_wire", ()-> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
//SlashBlade
    public static final RegistryObject<Item> STREDGEUNIVERSE = item("stredgeuniverse", StredgeuniverseItem::new);
//BloodMagic
    public static final RegistryObject<Item> blood_orb_of_armok = item("blood_orb_of_armok", () -> new BloodOrbOfArmokItem(() -> new BloodOrb(new ResourceLocation(AvaritiaIntegration.MOD_ID), 1,1000000000, 10)));
//EnderIO
    public static final RegistryObject<Item> infinity_capacitor = item("infinity_capacitor", () -> new InfinityCapacitorItem(InfinityCapacitorData.INSTANCE, new Item.Properties().rarity(ModRarities.EPIC)));
    public static final RegistryObject<Item> infinity_grinding_ball = item("infinity_grinding_ball", () -> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    public static final RegistryObject<Item> neutron_grinding_ball = item("neutron_grinding_ball", () -> new BaseItem(pro -> pro.rarity(ModRarities.RARE)));
//Create
    public static final RegistryObject<Item> creative_mechanism = item("creative_mechanism", () -> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));
    public static final RegistryObject<Item> creative_compound = item("creative_compound", () -> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));

//Applied Energistics
    public static final RegistryObject<Item> infinity_me_storage_component = item("infinity_me_storage_component", () -> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));

//Thermal Expansion
    public static final RegistryObject<Item> creative_augment_base = item("creative_augment_base", () -> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));

//PneumaticCraft: Repressurized
    public static final RegistryObject<Item> creative_compressed_iron = item("creative_compressed_iron", () -> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));

//Refined Storage
    public static final RegistryObject<Item> infinity_storge_part = item("infinity_storage_part", () -> new BaseItem(pro -> pro.rarity(ModRarities.EPIC)));


    public static RegistryObject<Item> item(String name) {
        return item(name, true);
    }

    public static RegistryObject<Item> item(String name, boolean exist) {
        return item(name, (e) -> new BaseItem(), exist);
    }

    public static RegistryObject<Item> item(String name, Function<String, Item> item) {
        return item(name, item, true);
    }

    public static RegistryObject<Item> item(String name, Function<String, Item> item, boolean exist) {
        return item(name, () -> item.apply(name), exist);
    }

    public static RegistryObject<Item> item(String name, Supplier<Item> item) {
        return item(name, item, true);
    }

    public static RegistryObject<Item> item(String name, Supplier<Item> item, boolean exist) {
        var regItem = ITEMS.register(name, item);

        return regItem;
    }
}
