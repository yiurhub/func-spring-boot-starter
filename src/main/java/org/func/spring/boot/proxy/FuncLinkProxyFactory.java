package org.func.spring.boot.proxy;

import org.func.spring.boot.container.FuncMethod;
import org.springframework.beans.factory.*;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @author Yiur
 */
public class FuncLinkProxyFactory<T> implements FactoryBean<T>, InitializingBean {

    private ApplicationContext context;

    private String beanName;

    private String[] refs;

    private List<FuncMethod> funcMethods;

    private final Class<T> interfaceType;

    private Class<T> classObject;

    public FuncLinkProxyFactory(Class<T> interfaceType) {
        this.interfaceType = interfaceType;
    }

    @Override
    public T getObject() throws Exception {
        FuncLinkProxy<T> funcLinkProxy = new FuncLinkProxy<>(classObject, context, beanName, refs, funcMethods);
        return newInstance(funcLinkProxy);
    }

    @SuppressWarnings("unchecked")
    private T newInstance(FuncLinkProxy<T> funcLinkProxy) {
        return (T) Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[] { interfaceType }, funcLinkProxy);
    }

    @Override
    public Class<T> getObjectType() {
        return interfaceType;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public void setRefs(String[] refs) {
        this.refs = refs;
    }

    public void setFuncMethods(List<FuncMethod> funcMethods) {
        this.funcMethods = funcMethods;
    }

    public void setClassObject(Class<T> classObject) {
        this.classObject = classObject;
    }

}
