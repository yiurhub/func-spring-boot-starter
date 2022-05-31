package org.func.spring.boot.component.callback;

import org.func.spring.boot.component.FuncLinkObject;

/**
 * An anonymous function executes a successful callback function
 * @author Yiur
 */
public interface FuncCallbackThen<T, R> extends FuncLinkObject {

    /**
     * invoke method
     * @param result The parameters returned by the anonymous function after execution
     * @return R
     */
    R then(T result);

}
