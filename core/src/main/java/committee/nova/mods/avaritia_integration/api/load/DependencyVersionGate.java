package committee.nova.mods.avaritia_integration.api.load;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

final class DependencyVersionGate {

    private DependencyVersionGate() {}

    static LoadDecision evaluate(String integrationModId, DependencyRule dependency, ArtifactVersion actualVersion) {
        String minVersion = dependency.normalizedMinVersion();
        String maxVersion = dependency.normalizedMaxVersion();
        String actual = actualVersion.toString();
        if (!minVersion.isEmpty() && actualVersion.compareTo(new DefaultArtifactVersion(minVersion)) < 0) {
            return new LoadDecision(integrationModId, LoadState.VERSION_TOO_LOW, dependency.modid(), minVersion,
                    maxVersion, actual, "Dependency version is too low: " + dependency.modid());
        }
        if (!maxVersion.isEmpty() && actualVersion.compareTo(new DefaultArtifactVersion(maxVersion)) > 0) {
            return new LoadDecision(integrationModId, LoadState.VERSION_TOO_HIGH, dependency.modid(), minVersion,
                    maxVersion, actual, "Dependency version is too high: " + dependency.modid());
        }
        return new LoadDecision(integrationModId, LoadState.LOADED, null, "", "", actual,
                "Integration is allowed to load: " + integrationModId);
    }
}
