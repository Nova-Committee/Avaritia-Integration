package committee.nova.mods.avaritia_integration.module;

import committee.nova.mods.avaritia_integration.api.load.DependencyRule;
import committee.nova.mods.avaritia_integration.api.load.IntegrationRule;

import java.util.List;

public interface ModModule {

    default IntegrationRule defaultLoadRule() {
        return new IntegrationRule(List.of());
    }

    static IntegrationRule rule(DependencyRule... dependencies) {
        return new IntegrationRule(List.of(dependencies));
    }

    static DependencyRule dependency(String modid) {
        return new DependencyRule(modid, "", "");
    }
}
