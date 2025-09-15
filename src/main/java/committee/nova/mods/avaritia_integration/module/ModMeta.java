package committee.nova.mods.avaritia_integration.module;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({/* No targets allowed */})
public @interface ModMeta {
    /*
    The target mod id
     */
    String id();

    String minVersion();

    String maxVersion();
}
