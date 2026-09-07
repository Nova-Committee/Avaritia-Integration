package committee.nova.mods.avaritia_integration.integrations.mekanism.common.config;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * BigInteger energy for infinity-tier Mekanism buffers. Mekanism 10.7 I/O is still {@code long};
 * values above {@link Long#MAX_VALUE} stay internal. {@link Double#MAX_VALUE} overflow count is
 * {@code energy / DOUBLE_MAX}.
 */
public final class MekBigEnergy {

    public static final String NBT_STORED_BIG = "storedBig";

    public static final BigInteger LONG_MAX = BigInteger.valueOf(Long.MAX_VALUE);
    public static final BigInteger DOUBLE_MAX = BigDecimal.valueOf(Double.MAX_VALUE).toBigInteger();
    /** Infinity buffer: one Double.MAX_VALUE unit plus one Long.MAX_VALUE remainder. */
    public static final BigInteger INFINITY_CAPACITY = DOUBLE_MAX.add(LONG_MAX);

    private MekBigEnergy() {}

    /** Per-tick infinity supply: Double.MAX_VALUE plus Long.MAX_VALUE. */
    public static BigInteger infinitySupplyTick() {
        return INFINITY_CAPACITY;
    }

    public static BigInteger doubleMaxOverflows(BigInteger energy) {
        if (energy.signum() <= 0) {
            return BigInteger.ZERO;
        }
        return energy.divide(DOUBLE_MAX);
    }

    public static BigInteger remainderAfterDoubleMax(BigInteger energy) {
        if (energy.signum() <= 0) {
            return BigInteger.ZERO;
        }
        return energy.remainder(DOUBLE_MAX);
    }

    public static long toMekanismLong(BigInteger energy) {
        if (energy.signum() <= 0) {
            return 0L;
        }
        if (energy.compareTo(LONG_MAX) >= 0) {
            return Long.MAX_VALUE;
        }
        return energy.longValue();
    }

    public static BigInteger fromMekanismLong(long energy) {
        if (energy <= 0L) {
            return BigInteger.ZERO;
        }
        return BigInteger.valueOf(energy);
    }

    /**
     * Long remainder for Mekanism {@code production - insert(...)} after a BigInteger insert.
     * Uses accepted energy, not leftover signum: a huge leftover can still mean Long.MAX_VALUE
     * was stored.
     */
    public static long longInsertRemainder(long requestedLong, BigInteger supplyRequested, BigInteger leftover) {
        BigInteger accepted = supplyRequested.subtract(leftover);
        if (accepted.signum() <= 0) {
            return requestedLong;
        }
        long acceptedLong = Math.min(requestedLong, toMekanismLong(accepted));
        return requestedLong - acceptedLong;
    }
}
