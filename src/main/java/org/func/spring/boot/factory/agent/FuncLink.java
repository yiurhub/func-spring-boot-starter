package org.func.spring.boot.factory.agent;

import org.func.spring.boot.properties.FuncProperties;
import org.func.spring.boot.utils.FuncString;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yiur
 */
public class FuncLink {

    private FuncProperties funcProperties;
    private Map<Object, Object> links;
    private Map<Object, Object> variables;

    public FuncLink(FuncProperties funcProperties) {
        this.links = new HashMap<>(10);
        this.variables = new HashMap<>(10);
        this.funcProperties = funcProperties;
    }

    public void setFuncProperties(FuncProperties funcProperties) {
        this.links = new HashMap<>(10);
        this.variables = new HashMap<>(10);
        this.funcProperties = funcProperties;
    }

    public Map<Object, Object> getObjects() {
        return links;
    }

    public Map<Object, Object> getVariables() {
        return variables;
    }

    @SuppressWarnings("unchecked")
    public <T> T getObject(@NonNull Class<T> classFile) {
        Assert.isTrue(funcProperties.isSingleton(), "The current mode is a singleton mode, it cannot be without the name attribute");
        return (T) links.get(classFile);
    }

    @SuppressWarnings("unchecked")
    public <T> T getObject(@NonNull Class<T> classFile, @NonNull String name) {
        return (T) links.get(name);
    }

    public <T> void setObject(@NonNull Class<T> classFile, @NonNull T lambda) {
        Assert.notNull(lambda, "classFile not fund");
        Assert.isTrue(funcProperties.isSingleton(), "The current mode is a singleton mode, it cannot be without the name attribute");
        Assert.isTrue(funcProperties.isLambda() && classFile.getDeclaredMethods().length >= 1, "The current class file must declare a method, and there must be less than one method : lambda is true");
        links.put(classFile, lambda);
    }

    public <T> FuncLink setObject(@NonNull Object key, @NonNull T lambda) {
        Assert.notNull(lambda, "classFile not fund");
        Assert.notNull(key, "key is null");
        Assert.isTrue(!key.equals(FuncString.EMPTY), "key is null");
        Assert.isTrue(funcProperties.isLambda() && lambda.getClass().getDeclaredMethods().length >= 1, "The current class file must declare a method, and there must be less than one method : lambda is true");
        links.put(key, lambda);
        return this;
    }

    @Deprecated
    public <T> FuncLink setObject(@NonNull Class<T> classFile, @NonNull Object key, @NonNull T lambda) {
        Assert.notNull(lambda, "classFile not fund");
        Assert.notNull(key, "key is null");
        Assert.isTrue(!key.equals(FuncString.EMPTY), "key is null");
        Assert.isTrue(funcProperties.isLambda() && classFile.getDeclaredMethods().length >= 1, "The current class file must declare a method, and there must be less than one method : lambda is true");
        links.put(key, lambda);
        return this;
    }

    public Object getVariable(@NonNull Object key) {
        return variables.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getVariable(@NonNull Class<T> tClass) {
        return (T) variables.get(tClass);
    }

    public FuncLink setVariable(@NonNull Object key, @NonNull Object value) {
        Assert.notNull(value, "object not fund");
        variables.put(key, value);
        return this;
    }

}
