package org.func.spring.boot.setting;

import org.func.spring.boot.link.FuncLink;
import org.func.spring.boot.method.FuncMethod;
import org.func.spring.boot.properties.FuncProperties;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author Yiur
 */
public class MethodSetting implements Setting, Serializable {

    private static final long serialVersionUID = 3504030773165980208L;

    private ApplicationContext context;

    private FuncProperties funcProperties;

    private FuncLink funcLink;

    private String beanName;

    private String alias;

    private String[] refs;

    private FuncMethod funcMethod;

    private Object proxy;

    private Object[] args;

    private Method proxyMethod;


    public MethodSetting() {
    }

    public MethodSetting setContext(ApplicationContext context) {
        this.context = context;
        return this;
    }

    public MethodSetting setFuncLink(FuncLink funcLink) {
        this.funcLink = funcLink;
        return this;
    }

    public MethodSetting setFuncProperties(FuncProperties funcProperties) {
        try {
            this.funcProperties = context.getBean(FuncProperties.class);
        } catch (BeansException beansException) {
            this.funcProperties = funcProperties;
        }
        return this;
    }

    public MethodSetting setBeanName(String beanName) {
        this.beanName = beanName;
        return this;
    }

    public MethodSetting setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public MethodSetting setRefs(String[] refs) {
        this.refs = refs;
        return this;
    }

    public MethodSetting setFuncMethod(FuncMethod funcMethod) {
        this.funcMethod = funcMethod;
        return this;
    }

    public MethodSetting setProxy(Object proxy) {
        this.proxy = proxy;
        return this;
    }

    public MethodSetting setArgs(Object[] args) {
        this.args = args;
        return this;
    }

    public MethodSetting setProxyMethod(Method proxyMethod) {
        this.proxyMethod = proxyMethod;
        return this;
    }

    public ApplicationContext getContext() {
        return context;
    }

    public FuncLink getFuncLink() {
        return funcLink;
    }

    public FuncProperties getFuncProperties() {
        return funcProperties;
    }

    public String getBeanName() {
        return beanName;
    }

    public String getAlias() {
        return alias;
    }

    public String[] getRefs() {
        return refs;
    }

    public FuncMethod getFuncMethod() {
        return funcMethod;
    }

    public Object getProxy() {
        return proxy;
    }

    public Object[] getArgs() {
        return args;
    }

    public Method getProxyMethod() {
        return proxyMethod;
    }

}
