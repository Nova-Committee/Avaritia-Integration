package committee.nova.mods.avaritia_integration.integrations.botania.botania.entity;

public final class InfinityManaPoolMath {

    private InfinityManaPoolMath() {}

    /**
     * Apply a signed mana delta without overflowing {@code int}. Result is clamped to
     * {@code [0, max]}.
     */
    public static int applyManaDelta(int current, int delta, int max) {
        if (max < 0) {
            max = 0;
        }
        long next = (long) current + (long) delta;
        if (next < 0L) {
            return 0;
        }
        if (next > max) {
            return max;
        }
        return (int) next;
    }
}
