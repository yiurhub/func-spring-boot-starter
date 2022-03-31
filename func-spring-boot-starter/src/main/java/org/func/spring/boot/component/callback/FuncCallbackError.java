package org.func.spring.boot.component.callback;

/**
 * The callback function after the anonymous function fails to execute
 * @author Yiur
 */
public interface FuncCallbackError<R> {

    /**
     * invoke method
     * @param e the returned exception object
     * @return R
     */
    R error(Throwable e);

}
