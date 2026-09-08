package committee.nova.mods.avaritia_integration.integrations.mekanism.common.util;

import net.neoforged.fml.ModList;

import fr.iglee42.evolvedmekanism.tiers.EMFactoryTier;
import mekanism.common.tier.FactoryTier;
import mekanism.common.util.EnumUtils;

import java.util.LinkedHashSet;
import java.util.Set;

public class MekIntegrationUtils {

    private MekIntegrationUtils() {}

    /**
     * Evolved Mekanism extends {@link FactoryTier} at runtime. {@link EnumUtils#FACTORY_TIERS} may already include
     * those values; never concatenate a second copy. Extra constants can also be null during early class init.
     */
    public static FactoryTier[] getFactoryTier() {
        Set<FactoryTier> tiers = new LinkedHashSet<>();
        addAll(tiers, EnumUtils.FACTORY_TIERS);
        if (ModList.get().isLoaded("evolvedmekanism")) {
            add(tiers, EMFactoryTier.OVERCLOCKED);
            add(tiers, EMFactoryTier.QUANTUM);
            add(tiers, EMFactoryTier.DENSE);
            add(tiers, EMFactoryTier.MULTIVERSAL);
            add(tiers, EMFactoryTier.CREATIVE);
        }
        return tiers.toArray(FactoryTier[]::new);
    }

    public static FactoryTier nextFactoryTier(FactoryTier tier) {
        FactoryTier[] tiers = getFactoryTier();
        for (int i = 0; i < tiers.length - 1; i++) {
            if (tiers[i] == tier) {
                return tiers[i + 1];
            }
        }
        return null;
    }

    private static void addAll(Set<FactoryTier> dest, FactoryTier[] source) {
        if (source == null) {
            return;
        }
        for (FactoryTier tier : source) {
            add(dest, tier);
        }
    }

    private static void add(Set<FactoryTier> dest, FactoryTier tier) {
        if (tier != null) {
            dest.add(tier);
        }
    }
}
