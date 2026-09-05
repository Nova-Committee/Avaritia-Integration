package committee.nova.mods.avaritia_integration.integrations.mekanism.common.capabilities.energy;

import committee.nova.mods.avaritia_integration.integrations.mekanism.common.config.MekBigEnergy;

import mekanism.api.Action;
import mekanism.api.AutomationType;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BigIntegerEnergyContainerTest {

    @Test
    void internalInsertStoresDoubleMaxPlusLongMax() {
        BigIntegerEnergyContainer container = BigIntegerEnergyContainer.output(MekBigEnergy.INFINITY_CAPACITY, null);
        assertEquals(0L, container.insert(1L, Action.EXECUTE, AutomationType.INTERNAL));
        assertEquals(MekBigEnergy.INFINITY_CAPACITY, container.getStoredBig());
        assertEquals(BigInteger.ONE, container.getDoubleMaxOverflows());
        assertEquals(Long.MAX_VALUE, container.getEnergy());
        assertEquals(0L, container.getNeeded());
    }

    @Test
    void extractCapsAtLongMax() {
        BigIntegerEnergyContainer container = BigIntegerEnergyContainer.output(MekBigEnergy.INFINITY_CAPACITY, null);
        container.insert(1L, Action.EXECUTE, AutomationType.INTERNAL);
        assertEquals(Long.MAX_VALUE, container.extract(Long.MAX_VALUE, Action.EXECUTE, AutomationType.MANUAL));
        assertEquals(MekBigEnergy.DOUBLE_MAX, container.getStoredBig());
        assertEquals(BigInteger.ONE, container.getDoubleMaxOverflows());
    }
}
