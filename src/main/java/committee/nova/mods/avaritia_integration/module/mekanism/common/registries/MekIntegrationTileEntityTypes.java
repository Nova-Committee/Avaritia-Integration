package committee.nova.mods.avaritia_integration.module.mekanism.common.registries;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.module.mekanism.common.content.blocktype.MekIntegrationFactoryType;
import committee.nova.mods.avaritia_integration.module.mekanism.common.tile.factory.TileEntityCollectingFactory;
import committee.nova.mods.avaritia_integration.module.mekanism.common.tile.factory.TileEntityCompressingFactory;
import committee.nova.mods.avaritia_integration.module.mekanism.common.tile.factory.TileEntityMIFactory;
import committee.nova.mods.avaritia_integration.module.mekanism.common.tile.machine.TileEntityNeutronCollector;
import committee.nova.mods.avaritia_integration.module.mekanism.common.tile.machine.TileEntitySingularityCompressor;
import committee.nova.mods.avaritia_integration.module.mekanism.common.util.MekIntegrationUtils;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.util.EnumUtils;

public class MekIntegrationTileEntityTypes {

    private MekIntegrationTileEntityTypes() {

    }
    public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(AvaritiaIntegration.MOD_ID);

    private static final Table<FactoryTier, MekIntegrationFactoryType, TileEntityTypeRegistryObject<? extends TileEntityMIFactory<?>>> FACTORIES = HashBasedTable.create();

    static {
        for (FactoryTier tier : MekIntegrationUtils.getFactoryTier()) {
            FACTORIES.put(tier, MekIntegrationFactoryType.NEUTRON_COLLECTING, TILE_ENTITY_TYPES.mekBuilder(MekIntegrationBlocks.getMekIntegrationFactory(tier, MekIntegrationFactoryType.NEUTRON_COLLECTING), (pos, state) -> new TileEntityCollectingFactory(MekIntegrationBlocks.getMekIntegrationFactory(tier, MekIntegrationFactoryType.NEUTRON_COLLECTING), pos, state)).serverTicker(TileEntityMekanism::tickServer).clientTicker(TileEntityMekanism::tickClient).build());
            FACTORIES.put(tier, MekIntegrationFactoryType.SINGULARITY_COMPRESSING, TILE_ENTITY_TYPES.mekBuilder(MekIntegrationBlocks.getMekIntegrationFactory(tier, MekIntegrationFactoryType.SINGULARITY_COMPRESSING), (pos, state) -> new TileEntityCompressingFactory(MekIntegrationBlocks.getMekIntegrationFactory(tier, MekIntegrationFactoryType.SINGULARITY_COMPRESSING), pos, state)).serverTicker(TileEntityMekanism::tickServer).clientTicker(TileEntityMekanism::tickClient).build());
        }
    }

    public static final TileEntityTypeRegistryObject<TileEntityNeutronCollector> NEUTRON_COLLECTOR = TILE_ENTITY_TYPES.mekBuilder(MekIntegrationBlocks.NEUTRON_COLLECTOR, TileEntityNeutronCollector::new).serverTicker(TileEntityMekanism::tickServer).clientTicker(TileEntityMekanism::tickClient).build();
    public static final TileEntityTypeRegistryObject<TileEntitySingularityCompressor> SINGULARITY_COMPRESSOR = TILE_ENTITY_TYPES.mekBuilder(MekIntegrationBlocks.SINGULARITY_COMPRESSOR, TileEntitySingularityCompressor::new).serverTicker(TileEntityMekanism::tickServer).clientTicker(TileEntityMekanism::tickClient).build();

    public static TileEntityTypeRegistryObject<? extends TileEntityMIFactory<?>> getFactoryTile(FactoryTier tier, MekIntegrationFactoryType type) {
        return FACTORIES.get(tier, type);
    }
}
