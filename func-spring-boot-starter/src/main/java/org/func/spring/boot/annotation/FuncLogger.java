package org.func.spring.boot.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * Whether to enable the output log
 * @author Yiur
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface FuncLogger {

    /**
     * log file name
     * @return String
     */
    @AliasFor("name")
    String value() default "";

    /**
     * enable func logger
     * @return boolean
     */
    boolean enable() default true;

    /**
     * path to log output
     * @return String
     */
    String path() default "";

    /**
     * log file name
     * @return String
     */
    @AliasFor("value")
    String name() default "";

    /**
     * custom log file suffix
     * @return String
     */
    String suffix() default "";

}
