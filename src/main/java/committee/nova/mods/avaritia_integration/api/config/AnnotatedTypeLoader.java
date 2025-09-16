package committee.nova.mods.avaritia_integration.api.config;

import com.google.common.io.Closeables;
import com.google.gson.Gson;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.forgespi.language.IModInfo;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author: cnlimiter
 */
public class AnnotatedTypeLoader implements Supplier<XConfiguration> {

    public final String modId;

    public AnnotatedTypeLoader(String modId) {
        this.modId = modId;
    }

    @Override
    public XConfiguration get() {
        Map<String, Object> properties = ModList.get().getModContainerById(modId).map(ModContainer::getModInfo).map(IModInfo::getModProperties).orElse(Collections.EMPTY_MAP);
        boolean useJson = (Boolean) properties.getOrDefault("xJsonMap", FMLEnvironment.production);
        if (!useJson) {
            return new DevEnvAnnotatedTypeLoader(modId).get();
        }
        String name = "/%s.x.json".formatted(modId);
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
        if (is == null) {
            return null;
        }
        InputStreamReader isr = new InputStreamReader(is);
        try {
            return new Gson().fromJson(isr, XConfiguration.class);
        } finally {
            Closeables.closeQuietly(isr);
            Closeables.closeQuietly(is);
        }
    }

}
