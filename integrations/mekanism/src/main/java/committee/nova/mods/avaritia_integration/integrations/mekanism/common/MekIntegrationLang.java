package committee.nova.mods.avaritia_integration.integrations.mekanism.common;

import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.text.ILangEntry;

@NothingNullByDefault
public enum MekIntegrationLang implements ILangEntry {
    NEUTRON_COLLECTING("factory.avaritia_integration.neutron_collecting"),
    SINGULARITY_COMPRESSING("factory.avaritia_integration.singularity_compressing"),
    DESCRIPTION_NEUTRON_COLLECTING("description.avaritia_integration.neutron_collector"),
    DESCRIPTION_SINGULARITY_COMPRESSING("description.avaritia_integration.singularity_compressor");

    private final String key;

    MekIntegrationLang(String key) {
        this.key = key;
    }

    @Override
    public String getTranslationKey() {
        return key;
    }
}
