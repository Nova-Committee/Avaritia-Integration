package committee.nova.mods.avaritia_integration.api.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import committee.nova.module.InModule;
import committee.nova.module.XAnnotationData;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModAnnotation;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.objectweb.asm.Type;

import java.util.Map;

/**
 * @author: cnlimiter
 */
public class DevEnvAnnotatedTypeLoader extends AnnotatedTypeLoader {

    public DevEnvAnnotatedTypeLoader(String modId) {
        super(modId);
    }

    @Override
    public XConfiguration get() {
        IModFileInfo modFileInfo = ModList.get().getModFileById(modId);
        if (modFileInfo == null)
            return null;

        final Type KIWI_MODULE = Type.getType(InModule.class);

        XConfiguration configuration = new XConfiguration();
        configuration.modules = Lists.newArrayList();

        for (ModFileScanData.AnnotationData annotationData : modFileInfo.getFile().getScanResult().getAnnotations()) {
            Type annotationType = annotationData.annotationType();
            if (KIWI_MODULE.equals(annotationType)) {
                configuration.modules.add(map(annotationData));
            }
        }
        return configuration;
    }

    private static XAnnotationData map(ModFileScanData.AnnotationData data) {
        Map<String, Object> annotationData = Maps.newHashMap();
        for (Map.Entry<String, Object> e : data.annotationData().entrySet()) {
            if (e.getValue() instanceof ModAnnotation.EnumHolder) {
                annotationData.put(e.getKey(), ((ModAnnotation.EnumHolder) e.getValue()).getValue());
            } else {
                annotationData.put(e.getKey(), e.getValue());
            }
        }
        return new XAnnotationData(data.clazz().getClassName(), annotationData);
    }
}
