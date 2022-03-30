package org.func.spring.boot.component.plugin;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.func.spring.boot.utils.ExceptionUtil;
import org.func.spring.boot.container.FuncMethod;
import org.func.spring.boot.factory.agent.FuncLink;
import org.func.spring.boot.properties.FuncProperties;
import org.springframework.lang.NonNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.func.spring.boot.utils.FuncString.format;

/**
 * funcLink plugins are executed by default
 * @author Yiur
 */
@Data
@NoArgsConstructor
public final class DefaultFuncInvokePlugin implements FuncLinkPlugin {

    private FuncLifePlugin funcLifePlugin;

    private FuncCallbackPlugin funcCallbackPlugin;

    private FuncLoggerPlugin funcLoggerPlugin;

    private Object proxy;

    private Object[] args;

    private Method objectMethod;

    private FuncMethod funcMethod;

    private String beanName;

    private String[] refs;

    private FuncLink funcLink;

    private FuncProperties funcProperties;

    public DefaultFuncInvokePlugin(String beanName, String[] refs, FuncLink funcLink, FuncProperties funcProperties) {
        this.beanName = beanName;
        this.refs = refs;
        this.funcLink = funcLink;
        this.funcProperties = funcProperties;
    }

    /**
     * func link method invoke
     * implementation process: life-start -&gt; proxy method -&gt; callback-then|callback-error -&gt; log -&gt; life-end
     * @return Object
     * @throws Throwable running
     */
    public Object invoke() throws Throwable {
        ExceptionUtil.notNullFuncMethod(funcMethod, objectMethod);
        funcLoggerPlugin = newInstance(funcPluginClass(FuncLoggerPlugin.class, DefaultFuncLoggerPlugin.class), beanName, refs, funcLink, funcProperties);
        funcCallbackPlugin = newInstance(funcPluginClass(FuncCallbackPlugin.class, DefaultFuncCallbackPlugin.class), beanName, refs, funcLink, funcProperties, funcLoggerPlugin);
        funcLifePlugin = newInstance(funcPluginClass(FuncLifePlugin.class, DefaultFuncLifePlugin.class), beanName, refs, funcLink, funcCallbackPlugin);
        return funcLifePlugin.life(funcMethod, proxy, objectMethod, args);
    }

    /**
     * Parse the FuncPlugin class.
     * The custom FuncPlugin has the highest priority.
     * If there is no custom FuncPlugin, the default implementation is returned.
     * <pre>
     *     funcLink.
     *                  &lt;Class&lt;? extends FuncPlugin&gt;&gt;setObject("bean:XXXPlugin", XXXPlugin.class);
     * </pre>
     * @param <T> type
     * @return type class
     */
    @SuppressWarnings("unchecked")
    private <T extends FuncLinkPlugin> Class<T> funcPluginClass(@NonNull Class<? extends FuncLinkPlugin> classFile, Class<? extends FuncLinkPlugin> defaultClass) {
        Object object = funcLink.getObject(classFile, format("?:?", beanName, classFile.getSimpleName()));
        if (object == null) {
            for (String ref : refs) {
                object = funcLink.getObject(classFile, format("?:?", ref, classFile.getSimpleName()));
                if (object != null) {
                    break;
                }
            }
        }
        Class<T> clazz;
        if (object == null) {
            clazz = (Class<T>) defaultClass;
        } else {
            clazz = (Class<T>) object;
        }
        return clazz;
    }

    /**
     * Create a new plugin object based on the parsed class
     * @param classObject class object
     * @param args constructor args
     * @param <T> type
     * @return type
     */
    @SuppressWarnings("unchecked")
    private <T extends FuncLinkPlugin> T newInstance(Class<T> classObject, Object... args) {
        Constructor<?>[] declaredConstructors = classObject.getDeclaredConstructors();
        T result = null;
        for (Constructor<?> declaredConstructor : declaredConstructors) {
            if (declaredConstructor.getParameterCount() == args.length) {
                try {
                    declaredConstructor.setAccessible(true);
                    result = (T) declaredConstructor.newInstance(args);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

}
