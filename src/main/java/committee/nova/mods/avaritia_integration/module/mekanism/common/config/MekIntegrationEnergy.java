package committee.nova.mods.avaritia_integration.module.mekanism.common.config;

import mekanism.api.math.FloatingLong;

public final class MekIntegrationEnergy {
    public static final FloatingLong NEUTRON_USAGE = FloatingLong.createConst(100_000L);
    public static final FloatingLong NEUTRON_STORAGE = FloatingLong.createConst(20_000_000L);
    public static final FloatingLong INFINITY_USAGE = FloatingLong.createConst(800_000L);
    public static final FloatingLong INFINITY_STORAGE = FloatingLong.createConst(160_000_000L);
    public static final FloatingLong NEUTRON_TABLET_RATE = FloatingLong.createConst(1_000_000_000_000L);
    public static final FloatingLong NEUTRON_TABLET_MAX = FloatingLong.createConst(1_000_000_000_000_000L);
    public static final FloatingLong INFINITY_TABLET_RATE = FloatingLong.createConst(Long.MAX_VALUE);
    public static final FloatingLong INFINITY_TABLET_MAX = FloatingLong.createConst(Long.MAX_VALUE);
    public static final long NEUTRON_GAS_TANK = 1_000_000L;
    public static final int NEUTRON_TICKS = 40;
    public static final int INFINITY_TICKS = 20;

    private MekIntegrationEnergy() {}
}
