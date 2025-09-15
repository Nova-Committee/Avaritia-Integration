package committee.nova.mods.avaritia_integration.api.module;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import committee.nova.mods.avaritia_integration.api.ModContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.CrashReportCallables;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author: cnlimiter
 */
public class XModules {
    private static Map<ResourceLocation, ModuleInfo> MODULES = Maps.newLinkedHashMap();
    private static final Set<ResourceLocation> LOADED_MODULES = Sets.newHashSet();


    static {
        CrashReportCallables.registerCrashCallable("Kiwi Modules", () -> ("\n" + LOADED_MODULES.stream().map(ResourceLocation::toString).sorted(StringUtils::compare).collect(Collectors.joining("\n\t\t", "\t\t", ""))));
    }

    private XModules() {
    }

    public static void add(ResourceLocation resourceLocation, AbModule module, ModContext context) {
        Preconditions.checkArgument(!isLoaded(resourceLocation), "Duplicate module: %s", resourceLocation);
        LOADED_MODULES.add(resourceLocation);
        MODULES.put(resourceLocation, new ModuleInfo(resourceLocation, module, context));
    }



    public static boolean isLoaded(ResourceLocation module) {
        return LOADED_MODULES.contains(module);
    }

    public static Collection<ModuleInfo> get() {
        return MODULES.values();
    }

    public static ModuleInfo get(ResourceLocation moduleId) {
        return MODULES.get(moduleId);
    }

    public static void clear() {
        MODULES.clear();
        MODULES = Map.of();
    }

    public static void fire(Consumer<ModuleInfo> consumer) {
        MODULES.values().forEach(consumer);
    }

    public static void fire(Consumer<ModuleInfo> consumer, Predicate<ModuleInfo> condition) {
        for (ModuleInfo info : get()) {
            if (condition.test(info)) {
                try {
                    consumer.accept(info);
                } catch (Exception e) {
                    XModule.LOGGER.error(XModule.MARKER, "Error firing event for module: " + info.module.uid, e);
                }
            }
        }
    }
}
