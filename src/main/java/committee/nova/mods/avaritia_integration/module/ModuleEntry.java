package committee.nova.mods.avaritia_integration.module;

import net.minecraftforge.api.distmarker.Dist;

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

    /*
    Which side should load
    */
    Dist[] side() default {Dist.CLIENT, Dist.DEDICATED_SERVER};
}
