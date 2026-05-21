package committee.nova.mods.avaritia_integration.api.load;

public record DependencyRule(String modid, String minVersion, String maxVersion) {

    public String normalizedMinVersion() {
        return this.minVersion == null ? "" : this.minVersion;
    }

    public String normalizedMaxVersion() {
        return this.maxVersion == null ? "" : this.maxVersion;
    }
}
