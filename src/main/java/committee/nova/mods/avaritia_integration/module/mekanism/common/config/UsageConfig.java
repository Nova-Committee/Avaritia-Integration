package committee.nova.mods.avaritia_integration.module.mekanism.common.config;

import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.value.CachedLongValue;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class UsageConfig extends BaseMekanismConfig {

    private final ModConfigSpec configSpec;

    public final CachedLongValue neutronCollector;
    public final CachedLongValue singularityCompressor;

    UsageConfig() {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.comment("Machine Energy Usage Config. This config is synced from server to client.").push("usage");

        neutronCollector = CachedLongValue.wrap(this, builder.comment("Energy per operation tick (Joules).").defineInRange("neutronCollector", 50L, 0L, Long.MAX_VALUE));
        singularityCompressor = CachedLongValue.wrap(this, builder.comment("Energy per operation tick (Joules).").defineInRange("singularityCompressor", 100L, 0L, Long.MAX_VALUE));

        builder.pop();
        configSpec = builder.build();
    }

    @Override
    public String getFileName() {
        return "machine-usage";
    }

    @Override
    public String getTranslation() {
        return "Avaritia Integration Mekanism Machine Usage Config";
    }

    @Override
    public ModConfigSpec getConfigSpec() {
        return configSpec;
    }

    @Override
    public ModConfig.Type getConfigType() {
        return ModConfig.Type.SERVER;
    }

    public boolean addToContainer() {
        return false;
    }
}
