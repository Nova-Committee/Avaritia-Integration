package committee.nova.mods.avaritia_integration.integrations.botania.botania.entity;

public final class AlphaSparkTransfers {

    public static final int TRANSFER_RATE = Integer.MAX_VALUE;
    /** Vanilla sparks scan 12 blocks. */
    public static final int SCAN_RANGE = 32;
    public static final int SCAN_RESCAN_INTERVAL = 20;

    private AlphaSparkTransfers() {}

    /**
     * Total mana this spark may move this tick across {@code connections} links, capped by
     * {@code availableMana}. Uses long arithmetic so
     * {@code TRANSFER_RATE * connections} cannot wrap negative.
     */
    public static int transferBudget(int connections, int availableMana) {
        if (connections <= 0 || availableMana <= 0) {
            return 0;
        }
        long total = (long) TRANSFER_RATE * (long) connections;
        if (total > Integer.MAX_VALUE) {
            total = Integer.MAX_VALUE;
        }
        return (int) Math.min(total, availableMana);
    }
}
