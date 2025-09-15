package committee.nova.mods.avaritia_integration.api.util;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

/**
 * @author: cnlimiter
 */
public class Util {
    @Nullable
    public static ResourceLocation RL(@Nullable String string, String defaultNamespace) {
        if (string != null && !string.contains(":")) {
            string = defaultNamespace + ":" + string;
        }
        return RL(string);
    }
    @Nullable
    public static ResourceLocation RL(@Nullable String string) {
        try {
            return ResourceLocation.tryParse(string);
        } catch (Exception e) {
            return null;
        }
    }

    public static String trimRL(ResourceLocation rl) {
        return trimRL(rl, "minecraft");
    }

    public static String trimRL(ResourceLocation rl, String defaultNamespace) {
        return rl.getNamespace().equals(defaultNamespace) ? rl.getPath() : rl.toString();
    }


}
