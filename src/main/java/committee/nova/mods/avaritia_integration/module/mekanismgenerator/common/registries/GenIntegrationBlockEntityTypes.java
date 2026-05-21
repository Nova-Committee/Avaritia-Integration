package committee.nova.mods.avaritia_integration.module.mekanismgenerator.common.registries;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.module.mekanismgenerator.common.blockentity.InfinityAdvancedSolarGeneratorBlockEntity;
import committee.nova.mods.avaritia_integration.module.mekanismgenerator.common.blockentity.InfinitySolarGeneratorBlockEntity;
import committee.nova.mods.avaritia_integration.module.mekanismgenerator.common.blockentity.NeutronAdvancedSolarGeneratorBlockEntity;
import committee.nova.mods.avaritia_integration.module.mekanismgenerator.common.blockentity.NeutronSolarGeneratorBlockEntity;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;

public class GenIntegrationBlockEntityTypes {

    private GenIntegrationBlockEntityTypes() {}

    public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(AvaritiaIntegration.MOD_ID);

    public static final TileEntityTypeRegistryObject<InfinitySolarGeneratorBlockEntity> INFINITY_SOLAR_GENERATOR = TILE_ENTITY_TYPES.mekBuilder(GenIntegrationBlocks.INFINITY_SOLAR_GENERATOR, InfinitySolarGeneratorBlockEntity::new)
            .clientTicker(TileEntityMekanism::tickClient)
            .serverTicker(TileEntityMekanism::tickServer)
            .withSimple(Capabilities.CONFIG_CARD)
            .build();
    public static final TileEntityTypeRegistryObject<NeutronSolarGeneratorBlockEntity> NEUTRON_SOLAR_GENERATOR = TILE_ENTITY_TYPES.mekBuilder(GenIntegrationBlocks.NEUTRON_SOLAR_GENERATOR, NeutronSolarGeneratorBlockEntity::new)
            .clientTicker(TileEntityMekanism::tickClient)
            .serverTicker(TileEntityMekanism::tickServer)
            .withSimple(Capabilities.CONFIG_CARD)
            .build();
    public static final TileEntityTypeRegistryObject<InfinityAdvancedSolarGeneratorBlockEntity> INFINITY_ADVANCED_SOLAR_GENERATOR = TILE_ENTITY_TYPES
            .mekBuilder(GenIntegrationBlocks.INFINITY_ADVANCED_SOLAR_GENERATOR, InfinityAdvancedSolarGeneratorBlockEntity::new)
            .clientTicker(TileEntityMekanism::tickClient)
            .serverTicker(TileEntityMekanism::tickServer)
            .withSimple(Capabilities.CONFIG_CARD)
            .withSimple(Capabilities.EVAPORATION_SOLAR)
            .build();
    public static final TileEntityTypeRegistryObject<NeutronAdvancedSolarGeneratorBlockEntity> NEUTRON_ADVANCED_SOLAR_GENERATOR = TILE_ENTITY_TYPES
            .mekBuilder(GenIntegrationBlocks.NEUTRON_ADVANCED_SOLAR_GENERATOR, NeutronAdvancedSolarGeneratorBlockEntity::new)
            .clientTicker(TileEntityMekanism::tickClient)
            .serverTicker(TileEntityMekanism::tickServer)
            .withSimple(Capabilities.CONFIG_CARD)
            .withSimple(Capabilities.EVAPORATION_SOLAR)
            .build();
}
