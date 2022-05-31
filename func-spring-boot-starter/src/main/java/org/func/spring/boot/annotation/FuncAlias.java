package org.func.spring.boot.annotation;

import java.lang.annotation.*;

/**
 * How to use class annotation deprecated when there are multiple beans in @FuncBean
 * which will cause alias duplication
 * @author Yiur
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface FuncAlias {

    /**
     * func link bean alias
     * @return String
     */
    String value() default "";

}
