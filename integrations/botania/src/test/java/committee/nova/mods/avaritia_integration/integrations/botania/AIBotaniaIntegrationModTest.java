package committee.nova.mods.avaritia_integration.integrations.botania;

import committee.nova.mods.avaritia_integration.api.load.DependencyRule;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AIBotaniaIntegrationModTest {

    @Test
    void defaultLoadRuleRequiresBotania455Snapshot() {
        DependencyRule botania = AIBotaniaIntegrationMod.createDefaultLoadRule().normalizedDependencies().getFirst();
        assertEquals(AIBotaniaIntegrationMod.DEPENDENCY_MOD_ID, botania.modid());
        assertEquals(AIBotaniaIntegrationMod.MIN_DEPENDENCY_VERSION, botania.normalizedMinVersion());
    }
}
