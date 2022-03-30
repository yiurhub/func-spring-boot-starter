package org.func.spring.boot.component.life;

import java.util.Map;

/**
 * Call the function before the anonymous function starts
 * @author Yiur
 */
public interface FuncLifeStart {

    /**
     * call function
     * @param args key(@FuncParameter) value pair
     * @return Map modified key value pair
     */
    Map<String, Object> start(Map<String, Object> args);

}
