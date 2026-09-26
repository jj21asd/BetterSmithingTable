package dev.jjblock21.bst;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RequiresMod {
    String[] value();

    /**
     * If true, only one of the specified mods is required
     */
    boolean any() default false;
}
