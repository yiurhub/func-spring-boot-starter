package org.func.spring.boot.filter;

import java.lang.reflect.Method;

/**
 * @author Yiur
 */
public interface ParseFilter {

    /**
     * parse filter
     * @param methods parse methods
     * @return Object
     * @exception Throwable t
     */
    Object parse(Method[] methods) throws Throwable;

}
