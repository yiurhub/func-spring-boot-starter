package org.func.spring.boot.annotation;

import java.lang.annotation.*;

/**
 * Whether to enable the output log
 * @author Yiur
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface FuncLogger {

    /**
     * path to log output
     * @return String
     */
    String path() default "";

    /**
     * log file name
     * @return String
     */
    String name() default "";

    /**
     * custom log file suffix
     * @return String
     */
    String suffix() default "";

}
