package org.func.spring.boot.annotation;

import java.lang.annotation.*;

/**
 * @author Yiur
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface ParseLog {

    /**
     * Parse the returned log in func logger according to bean name or ref
     * @return String
     */
    String value();

}
