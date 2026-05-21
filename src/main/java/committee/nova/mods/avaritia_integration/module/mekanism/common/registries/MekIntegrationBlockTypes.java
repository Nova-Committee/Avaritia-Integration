package committee.nova.mods.avaritia_integration.module.mekanism.common.registries;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import committee.nova.mods.avaritia_integration.module.mekanism.common.MekIntegrationLang;
import committee.nova.mods.avaritia_integration.module.mekanism.common.content.blocktype.MekIntegrationFactory;
import committee.nova.mods.avaritia_integration.module.mekanism.common.content.blocktype.MekIntegrationFactory.MekIntegrationFactoryBuilder;
import committee.nova.mods.avaritia_integration.module.mekanism.common.content.blocktype.MekIntegrationFactoryMachine;
import committee.nova.mods.avaritia_integration.module.mekanism.common.content.blocktype.MekIntegrationFactoryMachine.MekIntegrationMachineBuilder;
import committee.nova.mods.avaritia_integration.module.mekanism.common.content.blocktype.MekIntegrationFactoryType;
import committee.nova.mods.avaritia_integration.module.mekanism.common.tile.machine.TileEntityNeutronCollector;
import committee.nova.mods.avaritia_integration.module.mekanism.common.tile.machine.TileEntitySingularityCompressor;
import committee.nova.mods.avaritia_integration.module.mekanism.common.util.MekIntegrationEnumUtils;
import committee.nova.mods.avaritia_integration.module.mekanism.common.util.MekIntegrationUtils;
import mekanism.common.config.MekanismConfig;
import mekanism.common.block.attribute.AttributeSideConfig;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.registries.MekanismSounds;
import mekanism.common.tier.FactoryTier;
import mekanism.common.util.EnumUtils;

public class MekIntegrationBlockTypes {

    private MekIntegrationBlockTypes() {

    }

    private static final Table<FactoryTier, MekIntegrationFactoryType, MekIntegrationFactory<?>> FACTORIES = HashBasedTable.create();

    // Neutron Collector
    public static final MekIntegrationFactoryMachine<TileEntityNeutronCollector> NEUTRON_COLLECTOR = MekIntegrationMachineBuilder
            .createMekIntegrationFactoryMachine(() -> MekIntegrationTileEntityTypes.NEUTRON_COLLECTOR, MekIntegrationLang.DESCRIPTION_NEUTRON_COLLECTING, MekIntegrationFactoryType.NEUTRON_COLLECTING)
            .withGui(() -> MekIntegrationContainerTypes.NEUTRON_COLLECTOR)
            .withSound(MekanismSounds.CHEMICAL_CRYSTALLIZER)
            .withEnergyConfig(MekanismConfig.usage.chemicalCrystallizer, MekanismConfig.storage.chemicalCrystallizer)
            .with(AttributeSideConfig.create(TransmissionType.ITEM, TransmissionType.CHEMICAL, TransmissionType.ENERGY))
            .withComputerSupport("neutronCollector")
            .build();

    // Neutron Compressor
    public static final MekIntegrationFactoryMachine<TileEntitySingularityCompressor> SINGULARITY_COMPRESSOR = MekIntegrationMachineBuilder
            .createMekIntegrationFactoryMachine(() -> MekIntegrationTileEntityTypes.SINGULARITY_COMPRESSOR, MekIntegrationLang.DESCRIPTION_SINGULARITY_COMPRESSING, MekIntegrationFactoryType.SINGULARITY_COMPRESSING)
            .withGui(() -> MekIntegrationContainerTypes.SINGULARITY_COMPRESSOR)
            .withSound(MekanismSounds.CHEMICAL_CRYSTALLIZER)
            .withEnergyConfig(MekanismConfig.usage.chemicalCrystallizer, MekanismConfig.storage.chemicalCrystallizer)
            .with(AttributeSideConfig.ELECTRIC_MACHINE)
            .withComputerSupport("singularityCompressor")
            .build();

    static {
        for (FactoryTier tier : MekIntegrationUtils.getFactoryTier()) {
            for (MekIntegrationFactoryType type : MekIntegrationEnumUtils.FACTORY_TYPES) {
                FACTORIES.put(tier, type, MekIntegrationFactoryBuilder.createMekIntegrationFactory(() -> MekIntegrationTileEntityTypes.getFactoryTile(tier, type), type, tier).build());
            }
        }
    }

    public static MekIntegrationFactory<?> getFactory(FactoryTier tier, MekIntegrationFactoryType type) {
        return FACTORIES.get(tier, type);
    }
}
