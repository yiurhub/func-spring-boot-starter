package com.simple.web.callback;

import org.func.spring.boot.component.callback.FuncCallback;

/**
 * @author Yiur
 */
public class DefaultCallback implements FuncCallback {

    @Override
    public Object then(Object data) { return data; }

    @Override
    public Object error(Throwable error) {
        return error.getMessage();
    }

}
