package committee.nova.mods.avaritia_integration.api.load;

import java.util.List;

public record IntegrationRule(List<DependencyRule> dependencies) {

    public List<DependencyRule> normalizedDependencies() {
        return this.dependencies == null ? List.of() : this.dependencies;
    }
}
