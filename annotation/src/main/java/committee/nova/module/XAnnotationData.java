package committee.nova.module;

import java.util.Map;

/**
 * @author: cnlimiter
 */
public class XAnnotationData {
    String target;
    Map<String, Object> data;

    public XAnnotationData(String target, Map<String, Object> data) {
        this.target = target;
        this.data = data;
    }

    public String target() {
        return target;
    }

    public Map<String, Object> data() {
        return data == null ? Map.of() : data;
    }
}
