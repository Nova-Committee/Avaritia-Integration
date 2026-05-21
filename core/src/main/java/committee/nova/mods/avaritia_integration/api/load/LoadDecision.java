package committee.nova.mods.avaritia_integration.api.load;

public record LoadDecision(String integrationModId, LoadState state, String failedDependencyModId,
                           String requiredMinVersion, String requiredMaxVersion, String actualVersion,
                           String message) {

    public boolean shouldLoad() {
        return this.state == LoadState.LOADED;
    }
}
