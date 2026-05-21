package committee.nova.mods.avaritia_integration.api.load;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforgespi.language.IModInfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public final class IntegrationLoadApi {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type RULES_TYPE = new TypeToken<LinkedHashMap<String, IntegrationRule>>() {}.getType();
    private static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get()
            .resolve("avaritia")
            .resolve("integration")
            .resolve("load_rules.json");

    private static Map<String, IntegrationRule> rules;
    private static String loadError;

    private IntegrationLoadApi() {}

    public static boolean isIntegrationLoaded(String integrationModId) {
        return ModList.get().isLoaded(integrationModId);
    }

    public static boolean shouldLoad(String integrationModId) {
        return explain(integrationModId).shouldLoad();
    }

    public static LoadDecision explain(String integrationModId) {
        if (!isIntegrationLoaded(integrationModId)) {
            return decision(integrationModId, LoadState.INTEGRATION_MOD_MISSING, null, "", "", "???",
                    "Integration mod is not loaded: " + integrationModId);
        }
        Map<String, IntegrationRule> loadedRules = getRules();
        if (loadError != null) {
            return decision(integrationModId, LoadState.RULE_INVALID, null, "", "", "???", loadError);
        }
        IntegrationRule rule = loadedRules.get(integrationModId);
        if (rule == null) {
            return decision(integrationModId, LoadState.RULE_MISSING, null, "", "", "???",
                    "Missing integration load rule: " + integrationModId);
        }
        for (DependencyRule dependency : rule.normalizedDependencies()) {
            LoadDecision invalidDecision = validateDependencyRule(integrationModId, dependency);
            if (invalidDecision != null) return invalidDecision;
            String dependencyModId = dependency.modid();
            Optional<ArtifactVersion> optionalVersion = getModVersion(dependencyModId);
            if (optionalVersion.isEmpty()) {
                return decision(integrationModId, LoadState.DEPENDENCY_MISSING, dependencyModId,
                        dependency.normalizedMinVersion(), dependency.normalizedMaxVersion(), "???",
                        "Missing dependency mod: " + dependencyModId);
            }
            ArtifactVersion actualVersion = optionalVersion.get();
            String minVersion = dependency.normalizedMinVersion();
            if (!minVersion.isEmpty() && actualVersion.compareTo(new DefaultArtifactVersion(minVersion)) < 0) {
                return decision(integrationModId, LoadState.VERSION_TOO_LOW, dependencyModId, minVersion,
                        dependency.normalizedMaxVersion(), actualVersion.toString(),
                        "Dependency version is too low: " + dependencyModId);
            }
            String maxVersion = dependency.normalizedMaxVersion();
            if (!maxVersion.isEmpty() && actualVersion.compareTo(new DefaultArtifactVersion(maxVersion)) > 0) {
                return decision(integrationModId, LoadState.VERSION_TOO_HIGH, dependencyModId, minVersion,
                        maxVersion, actualVersion.toString(), "Dependency version is too high: " + dependencyModId);
            }
        }
        return decision(integrationModId, LoadState.LOADED, null, "", "", "???",
                "Integration is allowed to load: " + integrationModId);
    }

    public static Map<String, LoadDecision> explainAll() {
        Map<String, LoadDecision> result = new LinkedHashMap<>();
        getRules().keySet().forEach(id -> result.put(id, explain(id)));
        return Collections.unmodifiableMap(result);
    }

    public static synchronized void reload() {
        rules = loadRules();
    }

    private static synchronized Map<String, IntegrationRule> getRules() {
        if (rules == null) rules = loadRules();
        return rules;
    }

    private static Map<String, IntegrationRule> loadRules() {
        loadError = null;
        try {
            if (Files.notExists(CONFIG_PATH)) {
                Map<String, IntegrationRule> defaultRules = createDefaultRules();
                saveDefaultRules(defaultRules);
                return defaultRules;
            }
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH, StandardCharsets.UTF_8)) {
                Map<String, IntegrationRule> loadedRules = GSON.fromJson(reader, RULES_TYPE);
                if (loadedRules == null) return Map.of();
                return Collections.unmodifiableMap(new LinkedHashMap<>(loadedRules));
            }
        } catch (IOException | JsonParseException e) {
            loadError = "Failed to load integration rules from " + CONFIG_PATH + ": " + e.getMessage();
            AvaritiaIntegration.LOGGER.error(loadError, e);
            return Map.of();
        }
    }

    private static void saveDefaultRules(Map<String, IntegrationRule> defaultRules) throws IOException {
        Files.createDirectories(CONFIG_PATH.getParent());
        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH, StandardCharsets.UTF_8)) {
            GSON.toJson(defaultRules, RULES_TYPE, writer);
        }
    }

    private static Map<String, IntegrationRule> createDefaultRules() {
        Map<String, IntegrationRule> defaultRules = new LinkedHashMap<>();
        defaultRules.put("avaritia_integration_ae2", rule(dependency("ae2")));
        defaultRules.put("avaritia_integration_botania", rule(dependency("botania")));
        defaultRules.put("avaritia_integration_create", rule(dependency("create")));
        defaultRules.put("avaritia_integration_enderio", rule(dependency("enderio")));
        defaultRules.put("avaritia_integration_industrialforegoing", rule(dependency("industrialforegoing")));
        defaultRules.put("avaritia_integration_ifeu", rule(dependency("ifeu")));
        defaultRules.put("avaritia_integration_mekanism", rule(dependency("mekanism")));
        defaultRules.put("avaritia_integration_mekanism_generators", rule(dependency("mekanism"),
                dependency("mekanismgenerators")));
        defaultRules.put("avaritia_integration_pneumaticcraft", rule(dependency("pneumaticcraft")));
        defaultRules.put("avaritia_integration_refinedstorage", rule(dependency("refinedstorage")));
        defaultRules.put("avaritia_integration_tconstruct_data", rule(dependency("tconstruct")));
        defaultRules.put("avaritia_integration_thermal_expansion_data", rule(dependency("thermal_expansion")));
        return Collections.unmodifiableMap(defaultRules);
    }

    private static IntegrationRule rule(DependencyRule... dependencies) {
        return new IntegrationRule(java.util.List.of(dependencies));
    }

    private static DependencyRule dependency(String modid) {
        return new DependencyRule(modid, "", "");
    }

    private static LoadDecision validateDependencyRule(String integrationModId, DependencyRule dependency) {
        if (dependency == null || dependency.modid() == null || dependency.modid().isBlank()) {
            return decision(integrationModId, LoadState.RULE_INVALID, null, "", "", "???",
                    "Dependency rule must define a non-empty modid");
        }
        return null;
    }

    private static Optional<ArtifactVersion> getModVersion(String id) {
        return ModList.get().getModContainerById(id).map(ModContainer::getModInfo).map(IModInfo::getVersion);
    }

    private static LoadDecision decision(String integrationModId, LoadState state, String failedDependencyModId,
                                         String requiredMinVersion, String requiredMaxVersion, String actualVersion,
                                         String message) {
        return new LoadDecision(integrationModId, state, failedDependencyModId, requiredMinVersion,
                requiredMaxVersion, actualVersion, message);
    }
}
