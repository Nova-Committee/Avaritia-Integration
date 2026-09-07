package committee.nova.mods.avaritia_integration.integrations.mekanism.common.capabilities.energy;

import committee.nova.mods.avaritia_integration.integrations.mekanism.common.config.MekBigEnergy;

import mekanism.api.Action;
import mekanism.api.AutomationType;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BigIntegerEnergyContainerTest {

    @Test
    void internalLongInsertHonorsAmount() {
        BigIntegerEnergyContainer container = BigIntegerEnergyContainer.output(MekBigEnergy.INFINITY_CAPACITY, null);
        assertEquals(0L, container.insert(1L, Action.EXECUTE, AutomationType.INTERNAL));
        assertEquals(BigInteger.ONE, container.getStoredBig());
        assertEquals(BigInteger.ZERO, container.getDoubleMaxOverflows());
    }

    @Test
    void insertBigStoresDoubleMaxPlusLongMax() {
        BigIntegerEnergyContainer container = BigIntegerEnergyContainer.output(MekBigEnergy.INFINITY_CAPACITY, null);
        assertEquals(BigInteger.ZERO,
                container.insertBig(MekBigEnergy.infinitySupplyTick(), Action.EXECUTE, AutomationType.INTERNAL));
        assertEquals(MekBigEnergy.INFINITY_CAPACITY, container.getStoredBig());
        assertEquals(BigInteger.ONE, container.getDoubleMaxOverflows());
        assertEquals(Long.MAX_VALUE, container.getEnergy());
        assertEquals(0L, container.getNeeded());
    }

    @Test
    void extractCapsAtLongMax() {
        BigIntegerEnergyContainer container = BigIntegerEnergyContainer.output(MekBigEnergy.INFINITY_CAPACITY, null);
        container.insertBig(MekBigEnergy.infinitySupplyTick(), Action.EXECUTE, AutomationType.INTERNAL);
        assertEquals(Long.MAX_VALUE, container.extract(Long.MAX_VALUE, Action.EXECUTE, AutomationType.MANUAL));
        assertEquals(MekBigEnergy.DOUBLE_MAX, container.getStoredBig());
        assertEquals(BigInteger.ONE, container.getDoubleMaxOverflows());
    }

    @Test
    void partialBufferInsertBigReportsLongAccepted() {
        BigIntegerEnergyContainer container = BigIntegerEnergyContainer.output(MekBigEnergy.INFINITY_CAPACITY, null);
        container.insertBig(MekBigEnergy.DOUBLE_MAX, Action.EXECUTE, AutomationType.INTERNAL);
        BigInteger supply = MekBigEnergy.infinitySupplyTick();
        BigInteger leftover = container.insertBig(supply, Action.SIMULATE, AutomationType.INTERNAL);
        assertEquals(0L, MekBigEnergy.longInsertRemainder(Long.MAX_VALUE, supply, leftover));
        leftover = container.insertBig(supply, Action.EXECUTE, AutomationType.INTERNAL);
        assertEquals(MekBigEnergy.DOUBLE_MAX, leftover);
        assertEquals(MekBigEnergy.INFINITY_CAPACITY, container.getStoredBig());
    }
}
