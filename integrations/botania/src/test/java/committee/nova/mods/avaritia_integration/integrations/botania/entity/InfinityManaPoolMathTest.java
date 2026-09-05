package committee.nova.mods.avaritia_integration.integrations.botania.botania.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InfinityManaPoolMathTest {

    @Test
    void onePlusMaxSaturatesInsteadOfWrappingToZero() {
        assertEquals(Integer.MAX_VALUE,
                InfinityManaPoolMath.applyManaDelta(1, Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

    @Test
    void nearFullPlusTenMillionSaturates() {
        int nearFull = Integer.MAX_VALUE - 1;
        assertEquals(Integer.MAX_VALUE,
                InfinityManaPoolMath.applyManaDelta(nearFull, 10_000_000, Integer.MAX_VALUE));
    }

    @Test
    void negativeExtractClampsToZero() {
        assertEquals(500, InfinityManaPoolMath.applyManaDelta(1000, -500, Integer.MAX_VALUE));
        assertEquals(0, InfinityManaPoolMath.applyManaDelta(100, -1000, Integer.MAX_VALUE));
        assertEquals(0, InfinityManaPoolMath.applyManaDelta(0, Integer.MIN_VALUE, Integer.MAX_VALUE));
    }
}
