package committee.nova.mods.avaritia_integration.module;

import java.lang.annotation.*;

/**
 * Define a module
 *
 * @author IAFEnvoy
 */
@SuppressWarnings("unused")
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ModuleEntry {
    /*
    The id of this module
     */
    String id();

    /*
    When should this module load
     */
    ModMeta[] target() default {};
}
