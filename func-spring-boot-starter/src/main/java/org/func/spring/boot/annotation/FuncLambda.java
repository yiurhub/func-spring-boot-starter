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
    Class<?> value() default Object.class;

    /**
     * Managed class interface
     * classFile obsolete Replaced by value attribute in 1.1.38
     * @return Class
     */
    @Deprecated
    Class<?> classFile() default Object.class;

    /**
     * spring bean name
     * @return String
     */
    String bean() default "";

    /**
     * func link bean alias
     * the same is true for adding @FuncAlias annotation in 1.1.38
     * @return String
     */
    String alias() default "";

    /**
     * func link bean refs
     * the refs attribute is deprecated and replaced by @FuncRefs in 1.1.38
     * @return String
     */
    @Deprecated
    String[] refs() default {};

    /**
     * Whether to enable callback operation
     * @return boolean
     */
    boolean callback() default true;

}
