package committee.nova.mods.avaritia_integration.integrations.mekanism.common.config;

public final class MekIntegrationEnergy {

    private MekIntegrationEnergy() {}

    /**
     * Neutron collector: 100 kJ/t. Elite cable is 1.024 MJ/t; ultimate is 8.192 MJ/t.
     */
    public static final long NEUTRON_USAGE = 100_000L;
    /**
     * Neutron collector buffer: 200 ticks of usage (20 MJ).
     */
    public static final long NEUTRON_STORAGE = NEUTRON_USAGE * 200L;
    /**
     * Singularity compressor: 800 kJ/t, under an elite cable. Factories still drain per process.
     */
    public static final long INFINITY_USAGE = 800_000L;
    /**
     * Singularity compressor buffer: 200 ticks of usage (160 MJ).
     */
    public static final long INFINITY_STORAGE = INFINITY_USAGE * 200L;

    /** Neutron tablet charge: 1 TJ/t. Vanilla tablet is 5 kJ/t. */
    public static final long NEUTRON_TABLET_RATE = 1_000_000_000_000L;
    /** Neutron tablet capacity: 1 PJ. Vanilla tablet is 1 MJ. */
    public static final long NEUTRON_TABLET_MAX = 1_000_000_000_000_000L;
    public static final long INFINITY_TABLET_RATE = Long.MAX_VALUE;
    public static final long INFINITY_TABLET_MAX = Long.MAX_VALUE;

    /** Neutron solar generation: 10 GJ/t. Vanilla solar is 50 J/t. */
    public static final long NEUTRON_SOLAR_GENERATION = 10_000_000_000L;
    /** Neutron advanced solar generation: 100 GJ/t. Vanilla advanced is 300 J/t. */
    public static final long NEUTRON_ADVANCED_SOLAR_GENERATION = 100_000_000_000L;
    public static final long INFINITY_SOLAR_GENERATION = Long.MAX_VALUE;
    public static final long INFINITY_ADVANCED_SOLAR_GENERATION = Long.MAX_VALUE;

    /** Neutron solar buffer: 10 TJ. Vanilla solar is ~96 kJ. */
    public static final long NEUTRON_SOLAR_STORAGE = 10_000_000_000_000L;
    public static final long INFINITY_SOLAR_STORAGE = Long.MAX_VALUE;

    /** Neutron collector tank: 1,000,000 mB. Vanilla copy was 10,000 mB. */
    public static final long NEUTRON_CHEMICAL_TANK = 1_000_000L;

    /** Neutron collector / collecting factory: 2 seconds per operation. Vanilla machines are 10 seconds. */
    public static final int NEUTRON_TICKS = 40;
    /** Singularity compressor / compressing factory: 1 second per operation. */
    public static final int INFINITY_TICKS = 20;
}
