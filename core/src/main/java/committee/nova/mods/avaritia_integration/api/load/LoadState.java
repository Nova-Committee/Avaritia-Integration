package committee.nova.mods.avaritia_integration.api.load;

public enum LoadState {
    LOADED,
    INTEGRATION_MOD_MISSING,
    RULE_MISSING,
    DEPENDENCY_MISSING,
    VERSION_TOO_LOW,
    VERSION_TOO_HIGH,
    RULE_INVALID
}
