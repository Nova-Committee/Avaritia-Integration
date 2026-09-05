package committee.nova.mods.avaritia_integration.integrations.mekanism.common.registries;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.block.prefab.BlockMekIntegrationFactoryMachine.BlockMekIntegrationFactory;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.config.MekIntegrationEnergy;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.content.blocktype.MekIntegrationFactory;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.content.blocktype.MekIntegrationFactoryMachine;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.content.blocktype.MekIntegrationFactoryType;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.item.block.machine.ItemBlockMekIntegrationFactory;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.tile.factory.TileEntityChemicalToItemMIFactory;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.tile.factory.TileEntityMIFactory;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.tile.machine.TileEntityNeutronCollector;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.tile.machine.TileEntitySingularityCompressor;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.util.MekIntegrationEnumUtils;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.util.MekIntegrationUtils;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import mekanism.api.tier.ITier;
import mekanism.common.attachments.component.AttachedEjector;
import mekanism.common.attachments.component.AttachedSideConfig;
import mekanism.common.attachments.containers.ContainerType;
import mekanism.common.attachments.containers.chemical.ChemicalTanksBuilder;
import mekanism.common.attachments.containers.item.ItemSlotsBuilder;
import mekanism.common.block.attribute.AttributeTier;
import mekanism.common.block.prefab.BlockTile.BlockTileModel;
import mekanism.common.content.blocktype.BlockType;
import mekanism.common.item.block.ItemBlockTooltip;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registries.MekanismDataComponents;
import mekanism.common.resource.BlockResourceInfo;
import mekanism.common.tier.FactoryTier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class MekIntegrationBlocks {

    private MekIntegrationBlocks() {}

    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(AvaritiaIntegration.MOD_ID);

    private static final Table<FactoryTier, MekIntegrationFactoryType, BlockRegistryObject<BlockMekIntegrationFactory<?>, ItemBlockMekIntegrationFactory>> FACTORIES = HashBasedTable
            .create();

    static {
        // factories
        for (FactoryTier tier : MekIntegrationUtils.getFactoryTier()) {
            for (MekIntegrationFactoryType type : MekIntegrationEnumUtils.FACTORY_TYPES) {
                FACTORIES.put(tier, type, registerFactory(MekIntegrationBlockTypes.getFactory(tier, type)));
            }
        }
    }

    public static final BlockRegistryObject<BlockTileModel<TileEntityNeutronCollector, MekIntegrationFactoryMachine<TileEntityNeutronCollector>>, ItemBlockTooltip<BlockTileModel<TileEntityNeutronCollector, MekIntegrationFactoryMachine<TileEntityNeutronCollector>>>> NEUTRON_COLLECTOR = BLOCKS
            .register("neutron_collector",
                    () -> new BlockTileModel<>(MekIntegrationBlockTypes.NEUTRON_COLLECTOR,
                            properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())),
                    (block, properties) -> new ItemBlockTooltip<>(block, true,
                            properties.component(MekanismDataComponents.EJECTOR, AttachedEjector.DEFAULT)
                                    .component(MekanismDataComponents.SIDE_CONFIG, AttachedSideConfig.CRYSTALLIZER)))
            .forItemHolder(holder -> holder
                    .addAttachmentOnlyContainers(ContainerType.CHEMICAL,
                            () -> ChemicalTanksBuilder.builder()
                                    .addBasic(MekIntegrationEnergy.NEUTRON_CHEMICAL_TANK).build())
                    .addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                            .addChemicalFillSlot(0).addOutput().addEnergy().build()));
    public static final BlockRegistryObject<BlockTileModel<TileEntitySingularityCompressor, MekIntegrationFactoryMachine<TileEntitySingularityCompressor>>, ItemBlockTooltip<BlockTileModel<TileEntitySingularityCompressor, MekIntegrationFactoryMachine<TileEntitySingularityCompressor>>>> SINGULARITY_COMPRESSOR = BLOCKS
            .register("singularity_compressor",
                    () -> new BlockTileModel<>(MekIntegrationBlockTypes.SINGULARITY_COMPRESSOR,
                            properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())),
                    (block, properties) -> new ItemBlockTooltip<>(block, true,
                            properties.component(MekanismDataComponents.EJECTOR, AttachedEjector.DEFAULT)
                                    .component(MekanismDataComponents.SIDE_CONFIG,
                                            AttachedSideConfig.ELECTRIC_MACHINE)))
            .forItemHolder(holder -> holder.addAttachmentOnlyContainers(ContainerType.ITEM,
                    () -> ItemSlotsBuilder.builder().addInput(1).addOutput().addEnergy().build()));

    private static <
            TILE extends TileEntityMIFactory<?>> BlockRegistryObject<BlockMekIntegrationFactory<?>, ItemBlockMekIntegrationFactory> registerFactory(MekIntegrationFactory<TILE> type) {
        FactoryTier factoryTier = (FactoryTier) type.get(AttributeTier.class).tier();
        int processes = factoryTier.processes;
        Supplier<BlockMekIntegrationFactory<?>> blockSupplier = () -> new BlockMekIntegrationFactory<>(type);
        return MekIntegrationBlocks
                .<BlockMekIntegrationFactory<?>, ItemBlockMekIntegrationFactory>registerTieredBlock(type,
                        "_" + type.getMekIntegrationFactoryType().getRegistryNameComponent() + "_factory",
                        blockSupplier, ItemBlockMekIntegrationFactory::new)
                .forItemHolder(holder -> {
                    switch (type.getMekIntegrationFactoryType()) {
                        case NEUTRON_COLLECTING -> holder
                                .addAttachmentOnlyContainers(ContainerType.CHEMICAL, () -> {
                                    ChemicalTanksBuilder tanks = ChemicalTanksBuilder.builder();
                                    long capacity = TileEntityChemicalToItemMIFactory.MAX_CHEMICAL * processes;
                                    for (int i = 0; i < processes; i++) {
                                        tanks.addBasic(capacity);
                                    }
                                    return tanks.build();
                                }).addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                                        .addOutput(processes).addEnergy().build());
                        case SINGULARITY_COMPRESSING -> holder.addAttachmentOnlyContainers(ContainerType.ITEM,
                                () -> ItemSlotsBuilder.builder()
                                        .addBasicFactorySlots(processes, stack -> true).addEnergy().build());
                    }
                });
    }

    private static <BLOCK extends Block,
            ITEM extends BlockItem> BlockRegistryObject<BLOCK, ITEM> registerTieredBlock(BlockType type, String suffix,
                                                                                         Supplier<? extends BLOCK> blockSupplier,
                                                                                         java.util.function.BiFunction<BLOCK, Item.Properties, ITEM> itemCreator) {
        return registerTieredBlock(type.get(AttributeTier.class).tier(), suffix, blockSupplier, itemCreator);
    }

    private static <BLOCK extends Block,
            ITEM extends BlockItem> BlockRegistryObject<BLOCK, ITEM> registerTieredBlock(ITier tier, String suffix,
                                                                                         Supplier<? extends BLOCK> blockSupplier,
                                                                                         java.util.function.BiFunction<BLOCK, Item.Properties, ITEM> itemCreator) {
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
    public static BlockRegistryObject<BlockMekIntegrationFactory<?>, ItemBlockMekIntegrationFactory> getMekIntegrationFactory(@NotNull FactoryTier tier,
                                                                                                                              @NotNull MekIntegrationFactoryType type) {
        return FACTORIES.get(tier, type);
    }

    @SuppressWarnings("unchecked")
    public static BlockRegistryObject<BlockMekIntegrationFactory<?>, ItemBlockMekIntegrationFactory>[] getMekIntegrationFactoryBlocks() {
        return FACTORIES.values().toArray(new BlockRegistryObject[0]);
    }
}
