package committee.nova.mods.avaritia_integration.module.mekanism.common.util;

import committee.nova.mods.avaritia_integration.module.mekanism.common.content.blocktype.MekIntegrationFactoryType;
import fr.iglee42.evolvedmekanism.tiers.EMFactoryTier;
import mekanism.common.tier.FactoryTier;
import mekanism.common.util.EnumUtils;
import net.neoforged.fml.ModList;

public class MekIntegrationEnumUtils {

    private MekIntegrationEnumUtils() {

    }

    /**
     * Cached value of {@link MekIntegrationFactoryType#values()}. DO NOT MODIFY THIS LIST.
     */
    public static final MekIntegrationFactoryType[] FACTORY_TYPES = MekIntegrationFactoryType.values();

    /**
     * Cached value of {@link EMFactoryTier()}(If you load it). DO NOT MODIFY THIS LIST.
     */
    public static FactoryTier[] EM_TIERS;

    static {
        // Compatible wit Emek
        // 需要判断是否加载模组
        if (ModList.get().isLoaded("evolvedmekanism")) {
            EM_TIERS = new FactoryTier[] { EMFactoryTier.OVERCLOCKED, EMFactoryTier.QUANTUM, EMFactoryTier.DENSE, EMFactoryTier.MULTIVERSAL, EMFactoryTier.CREATIVE };
        }
    }
}
