package org.func.spring.boot.annotation;

import java.lang.annotation.*;

/**
 * Anonymous function hosting annotation
 * @author Yiur
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface FuncLambda {

    /**
     * Managed class interface
     * @return Class
     */
    Class<?> classFile();

    /**
     * spring bean name
     * @return String
     */
    String bean() default "";

    /**
     * refs
     * @return String
     */
    String[] refs() default {};

    /**
     * Whether to enable callback operation
     * @return boolean
     */
    boolean callback() default true;

}
