package committee.nova.mods.avaritia_integration.integrations.mekanism.common.registries;

import committee.nova.mods.avaritia_integration.integrations.mekanism.common.MekIntegrationLang;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.config.MekIntegrationEnergy;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.content.blocktype.MekIntegrationFactory;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.content.blocktype.MekIntegrationFactory.MekIntegrationFactoryBuilder;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.content.blocktype.MekIntegrationFactoryMachine;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.content.blocktype.MekIntegrationFactoryMachine.MekIntegrationMachineBuilder;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.content.blocktype.MekIntegrationFactoryType;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.tile.machine.TileEntityNeutronCollector;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.tile.machine.TileEntitySingularityCompressor;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.util.MekIntegrationEnumUtils;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.util.MekIntegrationUtils;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import mekanism.common.block.attribute.AttributeSideConfig;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.registries.MekanismSounds;
import mekanism.common.tier.FactoryTier;

public class MekIntegrationBlockTypes {

    private MekIntegrationBlockTypes() {}

    private static final Table<FactoryTier, MekIntegrationFactoryType, MekIntegrationFactory<?>> FACTORIES = HashBasedTable
            .create();

    // Neutron Collector
    public static final MekIntegrationFactoryMachine<TileEntityNeutronCollector> NEUTRON_COLLECTOR = MekIntegrationMachineBuilder
            .createMekIntegrationFactoryMachine(() -> MekIntegrationTileEntityTypes.NEUTRON_COLLECTOR,
                    MekIntegrationLang.DESCRIPTION_NEUTRON_COLLECTING, MekIntegrationFactoryType.NEUTRON_COLLECTING)
            .withGui(() -> MekIntegrationContainerTypes.NEUTRON_COLLECTOR)
            .withSound(MekanismSounds.CHEMICAL_CRYSTALLIZER)
            .withEnergyConfig(() -> MekIntegrationEnergy.NEUTRON_USAGE, () -> MekIntegrationEnergy.NEUTRON_STORAGE)
            .with(AttributeSideConfig.create(TransmissionType.ITEM, TransmissionType.CHEMICAL, TransmissionType.ENERGY))
            .withComputerSupport("neutronCollector")
            .build();

    // Neutron Compressor
    public static final MekIntegrationFactoryMachine<TileEntitySingularityCompressor> SINGULARITY_COMPRESSOR = MekIntegrationMachineBuilder
            .createMekIntegrationFactoryMachine(() -> MekIntegrationTileEntityTypes.SINGULARITY_COMPRESSOR,
                    MekIntegrationLang.DESCRIPTION_SINGULARITY_COMPRESSING,
                    MekIntegrationFactoryType.SINGULARITY_COMPRESSING)
            .withGui(() -> MekIntegrationContainerTypes.SINGULARITY_COMPRESSOR)
            .withSound(MekanismSounds.CHEMICAL_CRYSTALLIZER)
            .withEnergyConfig(() -> MekIntegrationEnergy.INFINITY_USAGE, () -> MekIntegrationEnergy.INFINITY_STORAGE)
            .with(AttributeSideConfig.ELECTRIC_MACHINE)
            .withComputerSupport("singularityCompressor")
            .build();

    static {
        for (FactoryTier tier : MekIntegrationUtils.getFactoryTier()) {
            for (MekIntegrationFactoryType type : MekIntegrationEnumUtils.FACTORY_TYPES) {
                FACTORIES.put(tier, type,
                        MekIntegrationFactoryBuilder
                                .createMekIntegrationFactory(
                                        () -> MekIntegrationTileEntityTypes.getFactoryTile(tier, type), type, tier)
                                .build());
            }
        }
    }

    public static MekIntegrationFactory<?> getFactory(FactoryTier tier, MekIntegrationFactoryType type) {
        return FACTORIES.get(tier, type);
    }
}
