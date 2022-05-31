package org.func.spring.boot.annotation;

import java.lang.annotation.*;

/**
 * @author Yiur
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface FuncRefs {

    /**
     * func link bean refs
     * @return String
     */
    String[] value() default {};

}
