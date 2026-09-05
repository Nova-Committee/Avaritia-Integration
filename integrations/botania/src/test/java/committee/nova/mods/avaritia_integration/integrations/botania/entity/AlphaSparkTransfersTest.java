package committee.nova.mods.avaritia_integration.integrations.botania.botania.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlphaSparkTransfersTest {

    @Test
    void oneConnectionUsesAvailableMana() {
        assertEquals(100, AlphaSparkTransfers.transferBudget(1, 100));
        assertEquals(Integer.MAX_VALUE, AlphaSparkTransfers.transferBudget(1, Integer.MAX_VALUE));
    }

    @Test
    void tenConnectionsStayPositiveAndSaturate() {
        int budget = AlphaSparkTransfers.transferBudget(10, Integer.MAX_VALUE);
        assertTrue(budget > 0);
        assertEquals(Integer.MAX_VALUE, budget);
    }

    @Test
    void elevenConnectionsStayPositiveAndSaturate() {
        int budget = AlphaSparkTransfers.transferBudget(11, Integer.MAX_VALUE);
        assertTrue(budget > 0);
        assertEquals(Integer.MAX_VALUE, budget);
        assertEquals(50, AlphaSparkTransfers.transferBudget(11, 50));
    }
}
