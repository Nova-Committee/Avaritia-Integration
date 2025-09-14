package committee.nova.mods.avaritia_integration.init.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;


public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AvaritiaIntegration.MOD_ID);
    public static final List<RegistryObject<Item>> ACCEPT_ITEM = new ArrayList<>();

    public static final RegistryObject<CreativeModeTab> CREATIVE_TAB = CREATIVE_TABS.register("avaritia_integration_group", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.tab.Integration"))
            .icon(()-> ModItems.infinity_gear.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModItems.blaze_cube_bolt.get());
                output.accept(ModItems.blaze_cube_dense_plate.get());
                output.accept(ModItems.blaze_cube_double_plate.get());
                output.accept(ModItems.blaze_cube_dust.get());
                output.accept(ModItems.blaze_cube_gear.get());
                output.accept(ModItems.blaze_cube_long_rod.get());
                output.accept(ModItems.blaze_cube_nugget.get());
                output.accept(ModItems.blaze_cube_plate.get());
                output.accept(ModItems.blaze_cube_ring.get());
                output.accept(ModItems.blaze_cube_rod.get());
                output.accept(ModItems.blaze_cube_screw.get());
                output.accept(ModItems.blaze_cube_spring.get());
                output.accept(ModItems.blaze_cube_wire.get());
                output.accept(ModItems.crystal_matrix_bolt.get());
                output.accept(ModItems.crystal_matrix_dense_plate.get());
                output.accept(ModItems.crystal_matrix_double_plate.get());
                output.accept(ModItems.crystal_matrix_dust.get());
                output.accept(ModItems.crystal_matrix_gear.get());
                output.accept(ModItems.crystal_matrix_long_rod.get());
                output.accept(ModItems.crystal_matrix_nugget.get());
                output.accept(ModItems.crystal_matrix_plate.get());
                output.accept(ModItems.crystal_matrix_ring.get());
                output.accept(ModItems.crystal_matrix_rod.get());
                output.accept(ModItems.crystal_matrix_screw.get());
                output.accept(ModItems.crystal_matrix_spring.get());
                output.accept(ModItems.crystal_matrix_wire.get());
                output.accept(ModItems.infinity_bolt.get());
                output.accept(ModItems.infinity_dense_plate.get());
                output.accept(ModItems.infinity_double_plate.get());
                output.accept(ModItems.infinity_dust.get());
                output.accept(ModItems.infinity_gear.get());
                output.accept(ModItems.infinity_long_rod.get());
                output.accept(committee.nova.mods.avaritia.init.registry.ModItems.infinity_nugget.get());
                output.accept(ModItems.infinity_plate.get());
                output.accept(ModItems.infinity_ring.get());
                output.accept(ModItems.infinity_rod.get());
                output.accept(ModItems.infinity_screw.get());
                output.accept(ModItems.infinity_spring.get());
                output.accept(ModItems.infinity_wire.get());
                output.accept(ModItems.neutron_bolt.get());
                output.accept(ModItems.neutron_dense_plate.get());
                output.accept(ModItems.neutron_double_plate.get());
                output.accept(ModItems.neutron_dust.get());
                output.accept(committee.nova.mods.avaritia.init.registry.ModItems.neutron_gear.get());
                output.accept(ModItems.neutron_long_rod.get());
                output.accept(committee.nova.mods.avaritia.init.registry.ModItems.neutron_nugget.get());
                output.accept(ModItems.neutron_plate.get());
                output.accept(ModItems.neutron_ring.get());
                output.accept(ModItems.neutron_rod.get());
                output.accept(ModItems.neutron_screw.get());
                output.accept(ModItems.neutron_spring.get());
                output.accept(ModItems.neutron_wire.get());
                output.accept(ModBlocks.asgard_dandelion.get());
                output.accept(ModBlocks.asgard_dandelion_floating.get());
                output.accept(ModBlocks.soarleander.get());
                output.accept(ModBlocks.soarleander_floating.get());
                output.accept(ModBlocks.infinity_mana_pool.get());
                output.accept(ModBlocks.infinity_potato.get());
                output.accept(ModItems.STREDGEUNIVERSE.get());
                output.accept(ModItems.blood_orb_of_armok.get());
                output.accept(ModItems.infinity_capacitor.get());
                output.accept(ModItems.neutron_grinding_ball.get());
                output.accept(ModItems.infinity_grinding_ball.get());
                output.accept(ModItems.creative_mechanism.get());
                output.accept(ModItems.creative_compound.get());
                output.accept(ModItems.infinity_me_storage_component.get());
                output.accept(ModItems.creative_augment_base.get());
                output.accept(ModItems.creative_compressed_iron.get());
                output.accept(ModItems.infinity_storge_part.get());

            })
            .build());

}
