package committee.nova.mods.avaritia_integration.integrations.mekanism.common.config;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MekBigEnergyTest {

    @Test
    void doubleMaxOverflowCountAndRemainder() {
        BigInteger energy = MekBigEnergy.DOUBLE_MAX.multiply(BigInteger.valueOf(3)).add(BigInteger.valueOf(42));
        assertEquals(BigInteger.valueOf(3), MekBigEnergy.doubleMaxOverflows(energy));
        assertEquals(BigInteger.valueOf(42), MekBigEnergy.remainderAfterDoubleMax(energy));
    }

    @Test
    void mekanismLongIsCapped() {
        assertEquals(Long.MAX_VALUE, MekBigEnergy.toMekanismLong(MekBigEnergy.DOUBLE_MAX));
        assertEquals(Long.MAX_VALUE, MekBigEnergy.toMekanismLong(MekBigEnergy.INFINITY_CAPACITY));
        assertEquals(7L, MekBigEnergy.toMekanismLong(BigInteger.valueOf(7)));
    }

    @Test
    void infinitySupplyIncludesDoubleMaxAndLongMax() {
        BigInteger supply = MekBigEnergy.infinitySupplyTick();
        assertEquals(MekBigEnergy.DOUBLE_MAX.add(MekBigEnergy.LONG_MAX), supply);
        assertTrue(supply.compareTo(MekBigEnergy.DOUBLE_MAX) > 0);
        assertTrue(supply.compareTo(MekBigEnergy.LONG_MAX) > 0);
        assertEquals(BigInteger.ONE, MekBigEnergy.doubleMaxOverflows(supply));
        assertEquals(MekBigEnergy.LONG_MAX, MekBigEnergy.remainderAfterDoubleMax(supply));
    }
}
