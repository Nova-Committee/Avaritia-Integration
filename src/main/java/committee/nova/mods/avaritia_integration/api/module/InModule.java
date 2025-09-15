package committee.nova.mods.avaritia_integration.api.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: cnlimiter
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface InModule {

    String value() default "core";

    String dependencies() default "";

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Subscriber {
        boolean clientOnly() default false;
        boolean modBus() default false;
    }
}
