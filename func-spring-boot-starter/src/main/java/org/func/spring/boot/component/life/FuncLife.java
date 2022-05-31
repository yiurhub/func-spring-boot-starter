package org.func.spring.boot.component.life;

import org.func.spring.boot.component.FuncLinkObject;
import org.func.spring.boot.exception.FuncLifeException;

import java.util.Map;

/**
 * Anonymous function life state call
 * @author Yiur
 */
public interface FuncLife<T, R> extends FuncLinkObject {

    /**
     * call function
     * @param args key(@FuncParameter) value pair
     * @return Map modified key value pair
     * @exception FuncLifeException t
     */
    Map<String, Object> start(Map<String, Object> args) throws FuncLifeException;
    /**
     * call function
     * @param result callback function return value
     * @return R
     * @exception FuncLifeException t
     */
    R end(T result) throws FuncLifeException;

}
