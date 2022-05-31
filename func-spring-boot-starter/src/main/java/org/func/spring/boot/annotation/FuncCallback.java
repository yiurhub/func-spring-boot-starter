package org.func.spring.boot.annotation;

import org.func.spring.boot.component.callback.SimpleFuncCallback;

import java.lang.annotation.*;

/**
 * Class to which the callback function is bound
 * @author Yiur
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface FuncCallback {

    /**
     * Class to which the callback function is bound
     * @return Class
     */
    Class<?> value() default SimpleFuncCallback.class;

}
