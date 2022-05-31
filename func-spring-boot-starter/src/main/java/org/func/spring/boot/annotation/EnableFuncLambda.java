package org.func.spring.boot.annotation;


import org.func.spring.boot.proxy.FuncLinkRegistryProcessor;
import org.func.spring.boot.proxy.init.FuncInitBeanRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Enable anonymous function proxy annotation (required)
 * @author Yiur
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ FuncInitBeanRegistrar.class, FuncLinkRegistryProcessor.class })
public @interface EnableFuncLambda {

    /**
     * The interface package name of the agent
     * @return String
     */
    String value();

}
