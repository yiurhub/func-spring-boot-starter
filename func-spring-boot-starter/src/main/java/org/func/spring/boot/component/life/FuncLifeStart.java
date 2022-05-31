package org.func.spring.boot.component.life;

import org.func.spring.boot.component.FuncLinkObject;
import org.func.spring.boot.exception.FuncLifeException;

import java.util.Map;

/**
 * Call the function before the anonymous function starts
 * @author Yiur
 */
public interface FuncLifeStart extends FuncLinkObject {

    /**
     * call function
     * @param args key(@FuncParameter) value pair
     * @return Map modified key value pair
     * @exception FuncLifeException t
     */
    Map<String, Object> start(Map<String, Object> args) throws FuncLifeException;

}
