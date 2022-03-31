package org.func.spring.boot.annotation;

import java.lang.annotation.*;

/**
 * Anonymous function configuration class annotation (required)
 * @author Yiur
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface FuncBean {

    /**
     * binding anonymous function linked beans
     * @return String
     */
    String link() default "";

}
