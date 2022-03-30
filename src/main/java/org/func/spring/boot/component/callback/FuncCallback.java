package org.func.spring.boot.component.callback;

/**
 * The callback function after the anonymous function is executed
 * @author Yiur
 */
public interface FuncCallback<T, R> {

    /**
     * An anonymous function executes a successful callback function
     * @param result The parameters returned by the anonymous function after execution
     * @return R
     */
    R then(T result);

    /**
     * The callback function after the anonymous function fails to execute
     * @param e the returned exception object
     * @return R
     */
    R error(Throwable e);

}
