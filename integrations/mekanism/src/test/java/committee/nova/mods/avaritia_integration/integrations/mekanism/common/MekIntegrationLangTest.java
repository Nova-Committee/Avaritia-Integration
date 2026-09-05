package committee.nova.mods.avaritia_integration.integrations.mekanism.common;

import committee.nova.mods.avaritia_integration.integrations.mekanism.common.content.blocktype.MekIntegrationFactoryType;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.registries.MekIntegrationBlockTypes;

import mekanism.api.text.ILangEntry;
import mekanism.common.content.blocktype.BlockType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MekIntegrationLangTest {

    @Test
    void langEnumExposesFourRealKeys() {
        assertEquals("factory.avaritia_integration.neutron_collecting",
                MekIntegrationLang.NEUTRON_COLLECTING.getTranslationKey());
        assertEquals("factory.avaritia_integration.singularity_compressing",
                MekIntegrationLang.SINGULARITY_COMPRESSING.getTranslationKey());
        assertEquals("description.avaritia_integration.neutron_collector",
                MekIntegrationLang.DESCRIPTION_NEUTRON_COLLECTING.getTranslationKey());
        assertEquals("description.avaritia_integration.singularity_compressor",
                MekIntegrationLang.DESCRIPTION_SINGULARITY_COMPRESSING.getTranslationKey());
        assertEquals(4, MekIntegrationLang.values().length);
    }

    @Test
    void factoryTypeLangEntriesAreNotNullCaptured() {
        assertEquals(MekIntegrationLang.NEUTRON_COLLECTING.getTranslationKey(),
                MekIntegrationFactoryType.NEUTRON_COLLECTING.getTranslationKey());
        assertEquals(MekIntegrationLang.SINGULARITY_COMPRESSING.getTranslationKey(),
                MekIntegrationFactoryType.SINGULARITY_COMPRESSING.getTranslationKey());
    }

    @Test
    void registeredMachineTypesExposeTranslatableDescriptions() {
        assertDescriptionTranslates(MekIntegrationBlockTypes.NEUTRON_COLLECTOR);
        assertDescriptionTranslates(MekIntegrationBlockTypes.SINGULARITY_COMPRESSOR);
    }

    private static void assertDescriptionTranslates(BlockType type) {
        ILangEntry description = type.getDescription();
        assertNotNull(description);
        assertNotNull(description.translate());
    }
}
