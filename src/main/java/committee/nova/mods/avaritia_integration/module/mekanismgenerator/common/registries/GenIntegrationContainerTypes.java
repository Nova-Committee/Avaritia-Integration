package committee.nova.mods.avaritia_integration.module.mekanismgenerator.common.registries;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.module.mekanismgenerator.common.blockentity.InfinityAdvancedSolarGeneratorBlockEntity;
import committee.nova.mods.avaritia_integration.module.mekanismgenerator.common.blockentity.InfinitySolarGeneratorBlockEntity;
import committee.nova.mods.avaritia_integration.module.mekanismgenerator.common.blockentity.NeutronAdvancedSolarGeneratorBlockEntity;
import committee.nova.mods.avaritia_integration.module.mekanismgenerator.common.blockentity.NeutronSolarGeneratorBlockEntity;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeDeferredRegister;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;

public class GenIntegrationContainerTypes {

    private GenIntegrationContainerTypes() {}

    public static final ContainerTypeDeferredRegister CONTAINER_TYPES = new ContainerTypeDeferredRegister(AvaritiaIntegration.MOD_ID);

    public static final ContainerTypeRegistryObject<MekanismTileContainer<InfinitySolarGeneratorBlockEntity>> INFINITY_SOLAR_GENERATOR = CONTAINER_TYPES.custom("infinity_solar_generator", InfinitySolarGeneratorBlockEntity.class).armorSideBar(-20, 11, 0).build();
    public static final ContainerTypeRegistryObject<MekanismTileContainer<NeutronSolarGeneratorBlockEntity>> NEUTRON_SOLAR_GENERATOR = CONTAINER_TYPES.custom("neutron_solar_generator", NeutronSolarGeneratorBlockEntity.class).armorSideBar(-20, 11, 0).build();
    public static final ContainerTypeRegistryObject<MekanismTileContainer<InfinityAdvancedSolarGeneratorBlockEntity>> INFINITY_ADVANCED_SOLAR_GENERATOR = CONTAINER_TYPES.custom("infinity_advanced_solar_generator", InfinityAdvancedSolarGeneratorBlockEntity.class).armorSideBar(-20, 11, 0).build();
    public static final ContainerTypeRegistryObject<MekanismTileContainer<NeutronAdvancedSolarGeneratorBlockEntity>> NEUTRON_ADVANCED_SOLAR_GENERATOR = CONTAINER_TYPES.custom("neutron_advanced_solar_generator", NeutronAdvancedSolarGeneratorBlockEntity.class).armorSideBar(-20, 11, 0).build();
}
