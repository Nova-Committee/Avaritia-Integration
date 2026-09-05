package committee.nova.mods.avaritia_integration.api.load;

import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DependencyVersionGateTest {

    private static final String INTEGRATION_ID = "avaritia_integration_botania";
    private static final DependencyRule BOTANIA_FLOOR = new DependencyRule("botania", "455-SNAPSHOT", "");

    @Test
    void rejectsBotaniaOlderThan455Snapshot() {
        LoadDecision decision = DependencyVersionGate.evaluate(INTEGRATION_ID, BOTANIA_FLOOR,
                new DefaultArtifactVersion("452-SNAPSHOT"));
        assertFalse(decision.shouldLoad());
        assertEquals(LoadState.VERSION_TOO_LOW, decision.state());
        assertEquals("botania", decision.failedDependencyModId());
        assertEquals("455-SNAPSHOT", decision.requiredMinVersion());
        assertEquals("452-SNAPSHOT", decision.actualVersion());
    }

    @Test
    void allowsBotania455SnapshotAndNewer() {
        assertTrue(DependencyVersionGate.evaluate(INTEGRATION_ID, BOTANIA_FLOOR,
                new DefaultArtifactVersion("455-SNAPSHOT")).shouldLoad());
        assertTrue(DependencyVersionGate.evaluate(INTEGRATION_ID, BOTANIA_FLOOR,
                new DefaultArtifactVersion("455-20260812.060955-47")).shouldLoad());
        assertTrue(DependencyVersionGate.evaluate(INTEGRATION_ID, BOTANIA_FLOOR,
                new DefaultArtifactVersion("456-SNAPSHOT")).shouldLoad());
    }
}
