package org.func.spring.boot.component.life;

import org.func.spring.boot.component.FuncLinkObject;
import org.func.spring.boot.exception.FuncLifeException;

/**
 * Call the function after the anonymous function starts
 * @author Yiur
 */
public interface FuncLifeEnd<T, R> extends FuncLinkObject {

    /**
     * call function
     * @param result callback function return value
     * @return R
     * @exception FuncLifeException t
     */
    R end(T result) throws FuncLifeException;

}
