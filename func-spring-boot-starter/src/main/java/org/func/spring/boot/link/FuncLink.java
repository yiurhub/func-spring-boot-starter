package org.func.spring.boot.link;

import org.func.spring.boot.component.FuncLinkObject;
import org.func.spring.boot.component.plugin.FuncLinkPlugin;
import org.func.spring.boot.properties.FuncProperties;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

import static org.func.spring.boot.utils.StringUtil.format;

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
    public <T> T getObject(@NonNull Class<T> tClass) {
        Assert.isTrue(funcProperties.isSingleton(), "The current mode is a singleton mode, it cannot be without the name attribute");
        return (T) links.get(tClass);
    }

    @SuppressWarnings("unchecked")
    public <T> T getObject(@NonNull String key) {
        return (T) links.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getObject(@NonNull Class<T> tClass, @NonNull String key) {
        return (T) links.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getObject(@NonNull String bean, @NonNull String link) {
        return (T) links.get(format("?:?", bean, link));
    }

    @SuppressWarnings("unchecked")
    public <T> T getObject(@NonNull Class<T> tClass, @NonNull String bean, @NonNull String link) {
        return (T) links.get(format("?:?", bean, link));
    }

    public <T extends FuncLinkObject> FuncLink setObject(@NonNull Object key, @NonNull T lambda) {
        Assert.isTrue(funcProperties.isLambda() && lambda.getClass().getDeclaredMethods().length >= 1, "The current class file must declare a method, and there must be less than one method : lambda is true");
        links.put(key, lambda);
        return this;
    }

    public <T extends FuncLinkObject> FuncLink setObject(@NonNull String bean, @NonNull String link, @NonNull T lambda) {
        Assert.isTrue(funcProperties.isLambda() && lambda.getClass().getDeclaredMethods().length >= 1, "The current class file must declare a method, and there must be less than one method : lambda is true");
        links.put(format("?:?", bean, link), lambda);
        return this;
    }

    public <T extends FuncLinkPlugin> FuncLink setPlugin(@NonNull Object key, @NonNull T lambda) {
        links.put(key, lambda);
        return this;
    }

    public <T extends FuncLinkPlugin> FuncLink setPlugin(@NonNull Object key, @NonNull Class<T> tClass) {
        links.put(key, tClass);
        return this;
    }

    public <T extends FuncLinkPlugin> FuncLink setPlugin(@NonNull String bean, @NonNull String link, @NonNull Class<T> tClass) {
        links.put(format("?:?", bean, link), tClass);
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
        variables.put(key, value);
        return this;
    }

}
