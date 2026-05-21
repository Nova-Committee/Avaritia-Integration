package committee.nova.mods.avaritia_integration.module.mekanism.common.registries;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.module.mekanism.common.block.prefab.BlockMekIntegrationFactoryMachine.BlockMekIntegrationFactory;
import committee.nova.mods.avaritia_integration.module.mekanism.common.content.blocktype.MekIntegrationFactory;
import committee.nova.mods.avaritia_integration.module.mekanism.common.content.blocktype.MekIntegrationFactoryType;
import committee.nova.mods.avaritia_integration.module.mekanism.common.item.block.machine.ItemBlockMekIntegrationFactory;
import committee.nova.mods.avaritia_integration.module.mekanism.common.tile.factory.TileEntityMIFactory;
import committee.nova.mods.avaritia_integration.module.mekanism.common.tile.machine.TileEntityNeutronCollector;
import committee.nova.mods.avaritia_integration.module.mekanism.common.tile.machine.TileEntitySingularityCompressor;
import committee.nova.mods.avaritia_integration.module.mekanism.common.util.MekIntegrationEnumUtils;
import committee.nova.mods.avaritia_integration.module.mekanism.common.util.MekIntegrationUtils;
import mekanism.api.tier.ITier;
import mekanism.common.block.attribute.AttributeTier;
import mekanism.common.block.prefab.BlockTile.BlockTileModel;
import mekanism.common.content.blocktype.BlockType;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.item.block.ItemBlockTooltip;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.resource.BlockResourceInfo;
import mekanism.common.tier.FactoryTier;
import mekanism.common.util.EnumUtils;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class MekIntegrationBlocks {

    private MekIntegrationBlocks() {

    }

    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(AvaritiaIntegration.MOD_ID);

    private static final Table<FactoryTier, MekIntegrationFactoryType, BlockRegistryObject<BlockMekIntegrationFactory<?>, ItemBlockMekIntegrationFactory>> FACTORIES = HashBasedTable.create();

    static {
        // factories
        for (FactoryTier tier : MekIntegrationUtils.getFactoryTier()) {
            for (MekIntegrationFactoryType type : MekIntegrationEnumUtils.FACTORY_TYPES) {
                FACTORIES.put(tier, type, registerFactory(MekIntegrationBlockTypes.getFactory(tier, type)));
            }
        }
    }

    public static final BlockRegistryObject<BlockTileModel<TileEntityNeutronCollector, Machine<TileEntityNeutronCollector>>, ItemBlockTooltip<BlockTileModel<TileEntityNeutronCollector, Machine<TileEntityNeutronCollector>>>> NEUTRON_COLLECTOR = BLOCKS.registerDetails("neutron_collector", () -> new BlockTileModel<>(MekIntegrationBlockTypes.NEUTRON_COLLECTOR, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())));
    public static final BlockRegistryObject<BlockTileModel<TileEntitySingularityCompressor, Machine<TileEntitySingularityCompressor>>, ItemBlockTooltip<BlockTileModel<TileEntitySingularityCompressor, Machine<TileEntitySingularityCompressor>>>> SINGULARITY_COMPRESSOR = BLOCKS.registerDetails("singularity_compressor", () -> new BlockTileModel<>(MekIntegrationBlockTypes.SINGULARITY_COMPRESSOR, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())));


    private static <TILE extends TileEntityMIFactory<?>> BlockRegistryObject<BlockMekIntegrationFactory<?>, ItemBlockMekIntegrationFactory> registerFactory(MekIntegrationFactory<TILE> type) {
        return registerTieredBlock(type, "_" + type.getMekIntegrationFactoryType().getRegistryNameComponent() + "_factory", () -> new BlockMekIntegrationFactory<>(type), ItemBlockMekIntegrationFactory::new);
    }

    private static <BLOCK extends Block, ITEM extends BlockItem> BlockRegistryObject<BLOCK, ITEM> registerTieredBlock(BlockType type, String suffix,
                                                                                                                      Supplier<? extends BLOCK> blockSupplier, java.util.function.BiFunction<BLOCK, Item.Properties, ITEM> itemCreator) {
        return registerTieredBlock(type.get(AttributeTier.class).tier(), suffix, blockSupplier, itemCreator);
    }

    private static <BLOCK extends Block, ITEM extends BlockItem> BlockRegistryObject<BLOCK, ITEM> registerTieredBlock(ITier tier, String suffix,
                                                                                                                      Supplier<? extends BLOCK> blockSupplier, java.util.function.BiFunction<BLOCK, Item.Properties, ITEM> itemCreator) {
        return BLOCKS.register(tier.getBaseTier().getLowerName() + suffix, blockSupplier, itemCreator);
    }

    /**
     * Retrieves a Factory with a defined tier and recipe type.
     *
     * @param tier - tier to add to the Factory
     * @param type - recipe type to add to the Factory
     *
     * @return factory with defined tier and recipe type
     */
    public static BlockRegistryObject<BlockMekIntegrationFactory<?>, ItemBlockMekIntegrationFactory> getMekIntegrationFactory(@NotNull FactoryTier tier, @NotNull MekIntegrationFactoryType type) {
        return FACTORIES.get(tier, type);
    }

    @SuppressWarnings("unchecked")
    public static BlockRegistryObject<BlockMekIntegrationFactory<?>, ItemBlockMekIntegrationFactory>[] getMekIntegrationFactoryBlocks() {
        return FACTORIES.values().toArray(new BlockRegistryObject[0]);
    }
}
