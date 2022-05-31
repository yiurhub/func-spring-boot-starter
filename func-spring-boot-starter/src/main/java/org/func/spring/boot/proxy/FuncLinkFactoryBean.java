package org.func.spring.boot.proxy;

import org.func.spring.boot.method.FuncMethod;
import org.func.spring.boot.utils.ClassScanner;
import org.springframework.beans.factory.*;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yiur
 */
public class FuncLinkFactoryBean<T> implements FactoryBean<T>, InitializingBean {

    private ApplicationContext context;

    private String beanName;

    private String alias;

    private String[] refs;

    private List<FuncMethod> funcMethods;

    private final Class<T> interfaceType;

    private Class<T> classObject;

    public FuncLinkFactoryBean(Class<T> interfaceType) {
        this.funcMethods = new ArrayList<>();
        this.interfaceType = interfaceType;
    }

    @Override
    public T getObject() throws Exception {
        return newInstance(new FuncLinkProxy<>(context, classObject, beanName, alias, refs, funcMethods));
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
        Method[] methods = ClassScanner.getMethods(classObject);
        for (Method method : methods) {
            funcMethods.add(new FuncMethod(beanName, refs, method));
        }
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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
