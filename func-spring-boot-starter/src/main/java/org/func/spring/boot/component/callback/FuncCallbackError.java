package org.func.spring.boot.component.callback;

import org.func.spring.boot.component.FuncLinkObject;

/**
 * The callback function after the anonymous function fails to execute
 * @author Yiur
 */
public interface FuncCallbackError<R> extends FuncLinkObject {

    /**
     * invoke method
     * @param e the returned exception object
     * @return R
     */
    R error(Throwable e);

}
