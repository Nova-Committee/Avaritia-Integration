package committee.nova.mods.avaritia_integration.api.load;

import java.util.List;

public record IntegrationRule(List<DependencyRule> dependencies, Boolean enabled) {

    public IntegrationRule(List<DependencyRule> dependencies) {
        this(dependencies, null);
    }

    public boolean isEnabled() {
        return !Boolean.FALSE.equals(enabled);
    }

    public List<DependencyRule> normalizedDependencies() {
        return this.dependencies == null ? List.of() : this.dependencies;
    }
}
