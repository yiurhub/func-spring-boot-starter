package org.func.spring.boot.component.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * default simple anonymous function callback class
 * @author Yiur
 */
public class SimpleFuncCallback implements FuncCallback {

    /**
     * Logger
     */
    private final static Logger logger = LoggerFactory.getLogger(SimpleFuncCallback.class);

    @Override
    public Object then(Object result) {
        return result;
    }

    @Override
    public Object error(Throwable e) {
        logger.error(((InvocationTargetException) e).getTargetException().getMessage());
        return ((InvocationTargetException) e).getTargetException().getMessage();
    }

}
