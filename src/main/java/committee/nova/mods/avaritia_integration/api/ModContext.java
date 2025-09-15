package committee.nova.mods.avaritia_integration.api;

import com.google.common.collect.Maps;
import committee.nova.mods.avaritia_integration.api.module.XModule;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * @author: cnlimiter
 */
public class ModContext {
    public static final Map<String, ModContext> ALL_CONTEXTS = Maps.newHashMap();

    public ModContainer modContainer;

    public static ModContext get(String modid) {
        if (ALL_CONTEXTS.containsKey(modid)) {
            return ALL_CONTEXTS.get(modid);
        } else {
            ModContext context = new ModContext(modid);
            ALL_CONTEXTS.put(modid, context);
            return context;
        }
    }

    private ModContext(String modid) {
        Objects.requireNonNull(modid, "Cannot get name of kiwi module.");
        try {
            modContainer = ModList.get().getModContainerById(modid).get();
        } catch (NoSuchElementException e) {
            XModule.LOGGER.error("Cannot find mod container for modid {}", modid);
        }
    }

    public void setActiveContainer() {
        ModLoadingContext.get().setActiveContainer(modContainer);
    }
}
