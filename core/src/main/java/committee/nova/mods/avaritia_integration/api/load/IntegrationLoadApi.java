package committee.nova.mods.avaritia_integration.api.load;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforgespi.language.IModInfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.apache.maven.artifact.versioning.ArtifactVersion;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public final class IntegrationLoadApi {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type RULES_TYPE = new TypeToken<LinkedHashMap<String, IntegrationRule>>() {}.getType();
    private static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get()
            .resolve("avaritia").resolve("integration").resolve("load_rules.json");
    private static final LoadRuleStore STORE = new LoadRuleStore(CONFIG_PATH);
    private static final Map<String, IntegrationRule> REGISTERED_DEFAULT_RULES = new LinkedHashMap<>();
    private static final Map<String, LoadDecision> STARTUP_DECISIONS = new LinkedHashMap<>();
    private static final Map<String, Boolean> STARTUP_ENABLED = new LinkedHashMap<>();
    private static boolean generatedRulesMode;
    private static Map<String, IntegrationRule> rules;
    private static String revision;
    private static String loadError;

    private IntegrationLoadApi() {}

    public static boolean isIntegrationLoaded(String integrationModId) {
        return ModList.get().isLoaded(integrationModId);
    }

    /** Every registration and pack lookup uses the same decision for this process. */
    public static synchronized boolean shouldLoad(String integrationModId) {
        if (!STARTUP_DECISIONS.containsKey(integrationModId)) {
            STARTUP_DECISIONS.put(integrationModId, explain(integrationModId));
            STARTUP_ENABLED.put(integrationModId, configuredEnabled(integrationModId));
        }
        return STARTUP_DECISIONS.get(integrationModId).shouldLoad();
    }

    public static synchronized LoadDecision startupDecision(String integrationModId) {
        shouldLoad(integrationModId);
        return STARTUP_DECISIONS.get(integrationModId);
    }

    public static synchronized boolean needsRestart(String integrationModId, boolean enabled) {
        return STARTUP_ENABLED.containsKey(integrationModId) && STARTUP_ENABLED.get(integrationModId) != enabled;
    }

    public static synchronized boolean configuredEnabled(String integrationModId) {
        IntegrationRule rule = getRules().get(integrationModId);
        return rule == null || rule.isEnabled();
    }

    public static synchronized LoadDecision explain(String integrationModId) {
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
        if (!rule.isEnabled()) {
            return decision(integrationModId, LoadState.DISABLED, null, "", "", "???",
                    "Integration is disabled by configuration: " + integrationModId);
        }
        // Required integration content cannot be restored by removing dependencies from an authored rule.
        IntegrationRule defaults = REGISTERED_DEFAULT_RULES.get(integrationModId);
        if (defaults != null) {
            for (DependencyRule dependency : defaults.normalizedDependencies()) {
                if (dependency.modid().startsWith("avaritia_integration_") &&
                        (!isIntegrationLoaded(dependency.modid()) || !configuredEnabled(dependency.modid()) ||
                                (!generatedRulesMode && !loadedRules.containsKey(dependency.modid())))) {
                    return decision(integrationModId, LoadState.DEPENDENCY_MISSING, dependency.modid(), "", "", "???",
                            "Required integration is missing or disabled: " + dependency.modid());
                }
            }
        }
        for (DependencyRule dependency : rule.normalizedDependencies()) {
            if (dependency == null || dependency.modid() == null || dependency.modid().isBlank()) {
                return decision(integrationModId, LoadState.RULE_INVALID, null, "", "", "???",
                        "Dependency rule must define a non-empty modid");
            }
            String dependencyModId = dependency.modid();
            Optional<ArtifactVersion> optionalVersion = getModVersion(dependencyModId);
            if (optionalVersion.isEmpty()) {
                return decision(integrationModId, LoadState.DEPENDENCY_MISSING, dependencyModId,
                        dependency.normalizedMinVersion(), dependency.normalizedMaxVersion(), "???",
                        "Missing dependency mod: " + dependencyModId);
            }
            LoadDecision versionDecision = DependencyVersionGate.evaluate(integrationModId, dependency,
                    optionalVersion.get());
            if (!versionDecision.shouldLoad()) return versionDecision;
        }
        return decision(integrationModId, LoadState.LOADED, null, "", "", "???",
                "Integration is allowed to load: " + integrationModId);
    }

    public static synchronized Map<String, LoadDecision> explainAll() {
        Map<String, LoadDecision> result = new LinkedHashMap<>();
        REGISTERED_DEFAULT_RULES.keySet().forEach(id -> result.put(id, explain(id)));
        getRules().keySet().forEach(id -> result.putIfAbsent(id, explain(id)));
        return Collections.unmodifiableMap(result);
    }

    public static synchronized Map<String, IntegrationRule> configuredRules() {
        return getRules();
    }

    public static synchronized String revision() {
        getRules();
        return revision;
    }

    public static synchronized String loadError() {
        getRules();
        return loadError;
    }

    /** Re-read next-launch configuration; never discard this process's registration decisions. */
    public static synchronized void reload() {
        rules = loadRules();
    }

    public static synchronized void initialize() {
        getRules();
    }

    /** Returns a directory-sync warning after a committed save, or null. */
    public static synchronized String saveEnabled(String expectedRevision, Map<String, Boolean> changes)
                                                                                                         throws IOException {
        if (loadError != null) throw new IOException(loadError);
        for (String id : changes.keySet()) {
            if (!getRules().containsKey(id)) throw new IOException("Missing integration rule: " + id);
        }
        String warning = STORE.saveEnabled(expectedRevision, changes);
        rules = loadRules();
        if (loadError != null) return "Saved, but cannot reload configuration: " + loadError;
        return warning;
    }

    public static synchronized void registerDefaultRule(String integrationModId, IntegrationRule rule) {
        if (integrationModId == null || integrationModId.isBlank() || rule == null) return;
        getRules();
        REGISTERED_DEFAULT_RULES.put(integrationModId, rule);
        if (generatedRulesMode && loadError == null) {
            Map<String, IntegrationRule> defaults = snapshotDefaultRules();
            try {
                JsonObject document = GSON.toJsonTree(defaults, RULES_TYPE).getAsJsonObject();
                String warning = STORE.save(revision, document);
                revision = GSON.toJson(document) + "\n";
                rules = defaults;
                if (warning != null) AvaritiaIntegration.LOGGER.warn(warning);
            } catch (IOException e) {
                loadError = "Failed to save integration rules to " + CONFIG_PATH + ": " + e.getMessage();
                AvaritiaIntegration.LOGGER.error(loadError, e);
            }
        }
    }

    private static Map<String, IntegrationRule> getRules() {
        if (rules == null) rules = loadRules();
        return rules;
    }

    private static Map<String, IntegrationRule> loadRules() {
        loadError = null;
        try {
            revision = STORE.read();
            generatedRulesMode = revision == null;
            if (generatedRulesMode) return snapshotDefaultRules();
            JsonObject document = JsonParser.parseString(revision).getAsJsonObject();
            for (var entry : document.entrySet()) {
                if (!entry.getValue().isJsonObject())
                    throw new JsonParseException("Rule must be an object: " + entry.getKey());
                var enabled = entry.getValue().getAsJsonObject().get("enabled");
                if (enabled != null && (!enabled.isJsonPrimitive() || !enabled.getAsJsonPrimitive().isBoolean())) {
                    throw new JsonParseException("enabled must be a boolean: " + entry.getKey());
                }
            }
            Map<String, IntegrationRule> loaded = GSON.fromJson(document, RULES_TYPE);
            return Collections.unmodifiableMap(new LinkedHashMap<>(loaded));
        } catch (IOException | JsonParseException | IllegalStateException e) {
            generatedRulesMode = false;
            loadError = "Failed to load integration rules from " + CONFIG_PATH + ": " + e.getMessage();
            AvaritiaIntegration.LOGGER.error(loadError, e);
            return Map.of();
        }
    }

    private static Map<String, IntegrationRule> snapshotDefaultRules() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(REGISTERED_DEFAULT_RULES));
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
