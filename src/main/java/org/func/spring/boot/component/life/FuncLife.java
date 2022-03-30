package org.func.spring.boot.component.life;

import java.util.Map;

/**
 * Anonymous function life state call
 * @author Yiur
 */
public interface FuncLife<T, R> {

    /**
     * call function
     * @param args key(@FuncParameter) value pair
     * @return Map modified key value pair
     */
    Map<String, Object> start(Map<String, Object> args);
    /**
     * call function
     * @param result callback function return value
     * @return R
     */
    R end(T result);

}
