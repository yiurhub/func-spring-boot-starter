package org.func.spring.boot.component.callback;

/**
 * default simple anonymous function callback class
 * @author Yiur
 */
public class SimpleFuncCallback implements FuncCallback {

    @Override
    public Object then(Object result) {
        return result;
    }

    @Override
    public Object error(Throwable e) {
        return e.getMessage();
    }

}
