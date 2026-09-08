package committee.nova.mods.avaritia_integration.integrations.gregtech.registry;

import com.gregtechceu.gtceu.api.fluid.FluidBuilder;
import com.gregtechceu.gtceu.api.fluid.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.material.material.Material;
import com.gregtechceu.gtceu.api.material.material.info.MaterialFlag;
import com.gregtechceu.gtceu.api.material.material.properties.BlastProperty;
import com.gregtechceu.gtceu.api.material.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.tag.TagPrefix;
import committee.nova.mods.avaritia.init.registry.ModBlocks;
import committee.nova.mods.avaritia.init.registry.ModItems;
import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.init.registry.AIFluids;
import committee.nova.mods.avaritia_integration.init.registry.AIItems;

import net.minecraft.world.level.ItemLike;

import static com.gregtechceu.gtceu.api.material.material.info.MaterialFlags.GENERATE_BOLT_SCREW;
import static com.gregtechceu.gtceu.api.material.material.info.MaterialFlags.GENERATE_FOIL;
import static com.gregtechceu.gtceu.api.material.material.info.MaterialFlags.GENERATE_FRAME;
import static com.gregtechceu.gtceu.api.material.material.info.MaterialFlags.GENERATE_GEAR;
import static com.gregtechceu.gtceu.api.material.material.info.MaterialFlags.GENERATE_PLATE;
import static com.gregtechceu.gtceu.api.material.material.info.MaterialFlags.GENERATE_ROD;
import static com.gregtechceu.gtceu.api.material.material.info.MaterialFlags.GENERATE_ROTOR;
import static com.gregtechceu.gtceu.api.material.material.info.MaterialFlags.GENERATE_SMALL_GEAR;
import static com.gregtechceu.gtceu.api.material.material.info.MaterialFlags.GENERATE_SPRING;
import static com.gregtechceu.gtceu.api.material.material.info.MaterialFlags.GENERATE_SPRING_SMALL;
import static com.gregtechceu.gtceu.api.tag.TagPrefix.block;
import static com.gregtechceu.gtceu.api.tag.TagPrefix.bolt;
import static com.gregtechceu.gtceu.api.tag.TagPrefix.dust;
import static com.gregtechceu.gtceu.api.tag.TagPrefix.dustSmall;
import static com.gregtechceu.gtceu.api.tag.TagPrefix.dustTiny;
import static com.gregtechceu.gtceu.api.tag.TagPrefix.gear;
import static com.gregtechceu.gtceu.api.tag.TagPrefix.gem;
import static com.gregtechceu.gtceu.api.tag.TagPrefix.gemChipped;
import static com.gregtechceu.gtceu.api.tag.TagPrefix.gemExquisite;
import static com.gregtechceu.gtceu.api.tag.TagPrefix.gemFlawed;
import static com.gregtechceu.gtceu.api.tag.TagPrefix.gemFlawless;
import static com.gregtechceu.gtceu.api.tag.TagPrefix.ingot;
import static com.gregtechceu.gtceu.api.tag.TagPrefix.nugget;
import static com.gregtechceu.gtceu.api.tag.TagPrefix.plate;
import static com.gregtechceu.gtceu.api.tag.TagPrefix.plateDense;
import static com.gregtechceu.gtceu.api.tag.TagPrefix.plateDouble;
import static com.gregtechceu.gtceu.api.tag.TagPrefix.rod;
import static com.gregtechceu.gtceu.api.tag.TagPrefix.rodLong;
import static com.gregtechceu.gtceu.api.tag.TagPrefix.screw;
import static com.gregtechceu.gtceu.api.tag.TagPrefix.spring;
import static com.gregtechceu.gtceu.api.tag.TagPrefix.wireFine;

public final class AIMaterials {

    public static Material Neutron;
    public static Material Infinity;
    public static Material CrystalMatrix;
    public static Material Blaze_Cube;
    public static Material Star_Fuel;

    private static final int MATERIAL_COLOR = 0xf4f4f4;
    private static final int BLAST_TEMP = 21800;
    private static final MaterialFlag[] INGOT_FLAGS = {
            GENERATE_PLATE, GENERATE_ROD, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_BOLT_SCREW,
            GENERATE_FOIL, GENERATE_ROTOR, GENERATE_SPRING, GENERATE_SPRING_SMALL, GENERATE_FRAME
    };

    private AIMaterials() {}

    public static void init() {
        Neutron = new Material.Builder(AvaritiaIntegration.rl("neutron"))
                .ingot()
                .liquid(new FluidBuilder().textures(true, true))
                .blastTemp(BLAST_TEMP, BlastProperty.GasTier.HIGHEST)
                .element(AIElements.Neutron)
                .color(MATERIAL_COLOR)
                .flags(INGOT_FLAGS)
                .buildAndRegister();

        Infinity = new Material.Builder(AvaritiaIntegration.rl("infinity"))
                .ingot()
                .liquid(new FluidBuilder().textures(true, true))
                .plasma(new FluidBuilder().textures(true, true))
                .fluid(FluidStorageKeys.MOLTEN, new FluidBuilder().textures(true, true))
                .blastTemp(BLAST_TEMP, BlastProperty.GasTier.HIGHEST)
                .element(AIElements.Infinity)
                .color(MATERIAL_COLOR)
                .flags(INGOT_FLAGS)
                .buildAndRegister();

        CrystalMatrix = new Material.Builder(AvaritiaIntegration.rl("crystal_matrix"))
                .ingot()
                .liquid(new FluidBuilder().textures(true, true))
                .plasma()
                .blastTemp(BLAST_TEMP, BlastProperty.GasTier.HIGHEST)
                .color(MATERIAL_COLOR)
                .flags(INGOT_FLAGS)
                .formula("Ψ₁₂C₄₈(SbP₈)₆E₈")
                .buildAndRegister();

        Blaze_Cube = new Material.Builder(AvaritiaIntegration.rl("blaze_cube"))
                .ingot()
                .liquid(new FluidBuilder().textures(true, true))
                .plasma()
                .blastTemp(BLAST_TEMP, BlastProperty.GasTier.HIGHEST)
                .color(MATERIAL_COLOR)
                .flags(INGOT_FLAGS)
                .formula("Ca₂WC₂S₄")
                .buildAndRegister();

        Star_Fuel = new Material.Builder(AvaritiaIntegration.rl("star_fuel"))
                .gem()
                .color(MATERIAL_COLOR)
                .formula("C")
                .buildAndRegister();

        ignoreReAvaritiaItems();
        replaceItem();
        storeExistingMoltenFluids();
    }

    private static void ignoreReAvaritiaItems() {
        excludeAllGems(Star_Fuel, ModItems.star_fuel);
        ignore(block, Star_Fuel, ModBlocks.star_fuel_block);

        ignore(ingot, Neutron, ModItems.neutron_ingot);
        ignore(nugget, Neutron, ModItems.neutron_nugget);
        ignore(block, Neutron, ModBlocks.neutron);
        ignore(gear, Neutron, ModItems.neutron_gear);

        ignore(ingot, CrystalMatrix, ModItems.crystal_matrix_ingot);
        ignore(block, CrystalMatrix, ModBlocks.crystal_matrix);

        ignore(ingot, Blaze_Cube, ModItems.blaze_cube);
        ignore(block, Blaze_Cube, ModBlocks.blaze_cube_block);

        ignore(ingot, Infinity, ModItems.infinity_ingot);
        ignore(nugget, Infinity, ModItems.infinity_nugget);
        ignore(block, Infinity, ModBlocks.infinity);
    }

    /**
     * 1.20.1 reassigned core part suppliers to GT items. DeferredItem cannot be replaced, so unify
     * those prefixes onto the fallback core items instead of generating colliding IDs.
     */
    public static void replaceItem() {
        ignore(plateDense, Blaze_Cube, AIItems.BLAZE_CUBE_DENSE_PLATE);
        ignore(dust, Blaze_Cube, AIItems.BLAZE_CUBE_DUST);
        ignore(gear, Blaze_Cube, AIItems.BLAZE_CUBE_GEAR);
        ignore(nugget, Blaze_Cube, AIItems.BLAZE_CUBE_NUGGET);
        ignore(plate, Blaze_Cube, AIItems.BLAZE_CUBE_PLATE);
        ignore(rod, Blaze_Cube, AIItems.BLAZE_CUBE_ROD);
        ignore(wireFine, Blaze_Cube, AIItems.BLAZE_CUBE_WIRE);
        ignore(bolt, Blaze_Cube, AIItems.BLAZE_CUBE_BOLT);
        ignore(screw, Blaze_Cube, AIItems.BLAZE_CUBE_SCREW);
        ignore(spring, Blaze_Cube, AIItems.BLAZE_CUBE_SPRING);
        ignore(plateDouble, Blaze_Cube, AIItems.BLAZE_CUBE_DOUBLE_PLATE);
        ignore(rodLong, Blaze_Cube, AIItems.BLAZE_CUBE_LONG_ROD);

        ignore(plateDense, CrystalMatrix, AIItems.CRYSTAL_MATRIX_DENSE_PLATE);
        ignore(dust, CrystalMatrix, AIItems.CRYSTAL_MATRIX_DUST);
        ignore(gear, CrystalMatrix, AIItems.CRYSTAL_MATRIX_GEAR);
        ignore(nugget, CrystalMatrix, AIItems.CRYSTAL_MATRIX_NUGGET);
        ignore(plate, CrystalMatrix, AIItems.CRYSTAL_MATRIX_PLATE);
        ignore(rod, CrystalMatrix, AIItems.CRYSTAL_MATRIX_ROD);
        ignore(wireFine, CrystalMatrix, AIItems.CRYSTAL_MATRIX_WIRE);
        ignore(bolt, CrystalMatrix, AIItems.CRYSTAL_MATRIX_BOLT);
        ignore(screw, CrystalMatrix, AIItems.CRYSTAL_MATRIX_SCREW);
        ignore(spring, CrystalMatrix, AIItems.CRYSTAL_MATRIX_SPRING);
        ignore(plateDouble, CrystalMatrix, AIItems.CRYSTAL_MATRIX_DOUBLE_PLATE);
        ignore(rodLong, CrystalMatrix, AIItems.CRYSTAL_MATRIX_LONG_ROD);

        ignore(plateDense, Infinity, AIItems.INFINITY_DENSE_PLATE);
        ignore(dust, Infinity, AIItems.INFINITY_DUST);
        ignore(gear, Infinity, AIItems.INFINITY_GEAR);
        ignore(plate, Infinity, AIItems.INFINITY_PLATE);
        ignore(rod, Infinity, AIItems.INFINITY_ROD);
        ignore(wireFine, Infinity, AIItems.INFINITY_WIRE);
        ignore(bolt, Infinity, AIItems.INFINITY_BOLT);
        ignore(screw, Infinity, AIItems.INFINITY_SCREW);
        ignore(spring, Infinity, AIItems.INFINITY_SPRING);
        ignore(plateDouble, Infinity, AIItems.INFINITY_DOUBLE_PLATE);
        ignore(rodLong, Infinity, AIItems.INFINITY_LONG_ROD);

        ignore(plateDense, Neutron, AIItems.NEUTRON_DENSE_PLATE);
        ignore(dust, Neutron, AIItems.NEUTRON_DUST);
        ignore(plate, Neutron, AIItems.NEUTRON_PLATE);
        ignore(rod, Neutron, AIItems.NEUTRON_ROD);
        ignore(wireFine, Neutron, AIItems.NEUTRON_WIRE);
        ignore(bolt, Neutron, AIItems.NEUTRON_BOLT);
        ignore(screw, Neutron, AIItems.NEUTRON_SCREW);
        ignore(spring, Neutron, AIItems.NEUTRON_SPRING);
        ignore(plateDouble, Neutron, AIItems.NEUTRON_DOUBLE_PLATE);
        ignore(rodLong, Neutron, AIItems.NEUTRON_LONG_ROD);
    }

    private static void storeExistingMoltenFluids() {
        Neutron.getProperty(PropertyKey.FLUID).getStorage().store(FluidStorageKeys.MOLTEN, AIFluids.source_molten_neutron, null);
        CrystalMatrix.getProperty(PropertyKey.FLUID).getStorage().store(FluidStorageKeys.MOLTEN, AIFluids.source_molten_crystal_matrix, null);
        Blaze_Cube.getProperty(PropertyKey.FLUID).getStorage().store(FluidStorageKeys.MOLTEN, AIFluids.source_molten_blaze, null);
    }

    private static void ignore(TagPrefix prefix, Material material, ItemLike item) {
        prefix.setIgnored(material, item);
    }

    private static void excludeAllGems(Material material, ItemLike... items) {
        gem.setIgnored(material, items);
        excludeAllGemsButNormal(material);
    }

    private static void excludeAllGemsButNormal(Material material) {
        gemChipped.setIgnored(material);
        gemFlawed.setIgnored(material);
        gemFlawless.setIgnored(material);
        gemExquisite.setIgnored(material);
        dust.setIgnored(material);
        dustSmall.setIgnored(material);
        dustTiny.setIgnored(material);
    }
}
