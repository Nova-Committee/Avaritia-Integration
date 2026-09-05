package committee.nova.mods.avaritia_integration.integrations.mekanism.common.config;

import committee.nova.mods.avaritia_integration.integrations.mekanism.common.content.blocktype.MekIntegrationFactoryType;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.registries.MekIntegrationBlockTypes;

import mekanism.common.block.attribute.AttributeEnergy;
import mekanism.common.tier.CableTier;
import mekanism.common.tier.FactoryTier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MekIntegrationEnergyTest {

    @Test
    void collectorAndCompressorBuffersAreFinite() {
        assertNotEquals(Long.MAX_VALUE, MekIntegrationEnergy.NEUTRON_STORAGE);
        assertNotEquals(Long.MAX_VALUE, MekIntegrationEnergy.INFINITY_STORAGE);
        assertTrue(MekIntegrationEnergy.NEUTRON_STORAGE > MekIntegrationEnergy.NEUTRON_USAGE);
        assertTrue(MekIntegrationEnergy.INFINITY_STORAGE > MekIntegrationEnergy.INFINITY_USAGE);
        assertTrue(MekIntegrationEnergy.NEUTRON_USAGE <= CableTier.ULTIMATE.getBaseCapacity());
        assertTrue(MekIntegrationEnergy.INFINITY_USAGE <= CableTier.ULTIMATE.getBaseCapacity());
        assertTrue(MekIntegrationEnergy.NEUTRON_USAGE < MekIntegrationEnergy.INFINITY_USAGE);
    }

    @Test
    void factoryEnergyStorageIncreasesWithTier() {
        assertStorageGrows(MekIntegrationFactoryType.NEUTRON_COLLECTING);
        assertStorageGrows(MekIntegrationFactoryType.SINGULARITY_COMPRESSING);
    }

    private static void assertStorageGrows(MekIntegrationFactoryType type) {
        long basic = storage(FactoryTier.BASIC, type);
        long elite = storage(FactoryTier.ELITE, type);
        long ultimate = storage(FactoryTier.ULTIMATE, type);
        assertTrue(basic < elite, type + " elite storage should exceed basic");
        assertTrue(elite < ultimate, type + " ultimate storage should exceed elite");
        assertNotEquals(Long.MAX_VALUE, ultimate);
    }

    private static long storage(FactoryTier tier, MekIntegrationFactoryType type) {
        AttributeEnergy energy = MekIntegrationBlockTypes.getFactory(tier, type).get(AttributeEnergy.class);
        assertNotNull(energy);
        return energy.getConfigStorage();
    }
}
