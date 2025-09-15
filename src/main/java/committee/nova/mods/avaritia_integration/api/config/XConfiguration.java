package committee.nova.mods.avaritia_integration.api.config;

import com.google.gson.annotations.SerializedName;
import committee.nova.mods.avaritia_integration.api.module.XAnnotationData;

import java.util.List;

/**
 * @author: cnlimiter
 */
public class XConfiguration {
    @SerializedName("XModule")
    public List<XAnnotationData> modules = List.of();
}
