package committee.nova.mods.avaritia_integration.module.mekanismgenerator.common.registries;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.module.mekanismgenerator.common.blockentity.InfinityAdvancedSolarGeneratorBlockEntity;
import committee.nova.mods.avaritia_integration.module.mekanismgenerator.common.blockentity.InfinitySolarGeneratorBlockEntity;
import committee.nova.mods.avaritia_integration.module.mekanismgenerator.common.blockentity.NeutronAdvancedSolarGeneratorBlockEntity;
import committee.nova.mods.avaritia_integration.module.mekanismgenerator.common.blockentity.NeutronSolarGeneratorBlockEntity;
import mekanism.common.attachments.containers.ContainerType;
import mekanism.common.attachments.containers.item.ItemSlotsBuilder;
import mekanism.common.block.prefab.BlockTile.BlockTileModel;
import mekanism.common.item.block.ItemBlockTooltip;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.generators.common.content.blocktype.Generator;
import net.minecraft.world.level.material.MapColor;

public class GenIntegrationBlocks {

    private GenIntegrationBlocks() {
    }

    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(AvaritiaIntegration.MOD_ID);

    public static final BlockRegistryObject<BlockTileModel<InfinitySolarGeneratorBlockEntity, Generator<InfinitySolarGeneratorBlockEntity>>, ItemBlockTooltip<BlockTileModel<InfinitySolarGeneratorBlockEntity, Generator<InfinitySolarGeneratorBlockEntity>>>> INFINITY_SOLAR_GENERATOR =
            BLOCKS.registerDetails("infinity_solar_generator", () -> new BlockTileModel<>(GenIntegrationBlockTypes.INFINITY_SOLAR_GENERATOR, properties -> properties.mapColor(MapColor.COLOR_BLUE)))
                    .forItemHolder(holder -> holder.addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder().addEnergy().build()));

    public static final BlockRegistryObject<BlockTileModel<NeutronSolarGeneratorBlockEntity, Generator<NeutronSolarGeneratorBlockEntity>>, ItemBlockTooltip<BlockTileModel<NeutronSolarGeneratorBlockEntity, Generator<NeutronSolarGeneratorBlockEntity>>>> NEUTRON_SOLAR_GENERATOR =
            BLOCKS.registerDetails("neutron_solar_generator", () -> new BlockTileModel<>(GenIntegrationBlockTypes.NEUTRON_SOLAR_GENERATOR, properties -> properties.mapColor(MapColor.COLOR_BLUE)))
                    .forItemHolder(holder -> holder.addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder().addEnergy().build()));

    public static final BlockRegistryObject<BlockTileModel<InfinityAdvancedSolarGeneratorBlockEntity, Generator<InfinityAdvancedSolarGeneratorBlockEntity>>, ItemBlockTooltip<BlockTileModel<InfinityAdvancedSolarGeneratorBlockEntity, Generator<InfinityAdvancedSolarGeneratorBlockEntity>>>> INFINITY_ADVANCED_SOLAR_GENERATOR =
            BLOCKS.registerDetails("infinity_advanced_solar_generator", () -> new BlockTileModel<>(GenIntegrationBlockTypes.INFINITY_ADVANCED_SOLAR_GENERATOR, properties -> properties.mapColor(MapColor.COLOR_BLUE)))
                    .forItemHolder(holder -> holder.addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder().addEnergy().build()));

    public static final BlockRegistryObject<BlockTileModel<NeutronAdvancedSolarGeneratorBlockEntity, Generator<NeutronAdvancedSolarGeneratorBlockEntity>>, ItemBlockTooltip<BlockTileModel<NeutronAdvancedSolarGeneratorBlockEntity, Generator<NeutronAdvancedSolarGeneratorBlockEntity>>>> NEUTRON_ADVANCED_SOLAR_GENERATOR =
            BLOCKS.registerDetails("neutron_advanced_solar_generator", () -> new BlockTileModel<>(GenIntegrationBlockTypes.NEUTRON_ADVANCED_SOLAR_GENERATOR, properties -> properties.mapColor(MapColor.COLOR_BLUE)))
                    .forItemHolder(holder -> holder.addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder().addEnergy().build()));
}
