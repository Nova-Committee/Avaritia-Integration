package committee.nova.mods.avaritia_integration.module.mekanism.common.util;

import mekanism.common.tier.FactoryTier;
import mekanism.common.util.EnumUtils;
import net.neoforged.fml.ModList;

import java.util.Arrays;

public class MekIntegrationUtils {

    private MekIntegrationUtils() {}

    /**
     * 为了适配EMek，由于它在mixin没有采用重载序列化与反序列化的操作，导致Mek的FactoryTier识别不到新加入的Tier
     * 因此需要在有Emek时将两个数组手动合并为一个。
     *
     * @return FactoryTier[]
     */
    public static FactoryTier[] getFactoryTier() {
        // Compatible wit Emek
        if (ModList.get().isLoaded("evolvedmekanism")) {
            FactoryTier[] mergedTiers;
            mergedTiers = Arrays.copyOf(EnumUtils.FACTORY_TIERS, EnumUtils.FACTORY_TIERS.length + MekIntegrationEnumUtils.EM_TIERS.length);
            System.arraycopy(MekIntegrationEnumUtils.EM_TIERS, 0, mergedTiers, EnumUtils.FACTORY_TIERS.length, MekIntegrationEnumUtils.EM_TIERS.length);
            return EnumUtils.FACTORY_TIERS;
        } else {
            return EnumUtils.FACTORY_TIERS;
        }
    }
}
