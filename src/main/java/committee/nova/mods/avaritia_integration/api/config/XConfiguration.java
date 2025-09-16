package committee.nova.mods.avaritia_integration.api.config;

import com.google.gson.annotations.SerializedName;
import committee.nova.module.XAnnotationData;

import java.util.List;

/**
 * @author: cnlimiter
 */
public class XConfiguration {
    @SerializedName("InModule")
    public List<XAnnotationData> modules = List.of();
}
