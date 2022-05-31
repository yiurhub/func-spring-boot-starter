package org.func.spring.boot.component.plugin;

import org.func.spring.boot.exception.FuncFinalException;
import org.func.spring.boot.setting.MethodSetting;

import java.util.Objects;

import static org.func.spring.boot.utils.StringUtil.format;

/**
 * funcLink plugins are executed by default
 * @author Yiur
 */
public final class FuncInvokePlugin {

    private FuncLifePlugin funcLifePlugin;

    private FuncCallbackPlugin funcCallbackPlugin;

    private FuncLoggerPlugin funcLoggerPlugin;

    private MethodSetting methodSetting;

    public FuncInvokePlugin(MethodSetting methodSetting) {
        this.methodSetting = methodSetting;
    }

    /**
     * func link method invoke
     * implementation process: life-start -&gt; proxy method -&gt; callback-then|callback-error -&gt; log -&gt; life-end
     * @return Object
     * @throws Throwable running
     */
    public Object invoke() throws Throwable {
        FuncFinalException.notNullFuncMethod(methodSetting.getFuncMethod(), methodSetting.getProxyMethod());
        funcLoggerPlugin = funcPluginClass(FuncLoggerPlugin.class, DefaultFuncLoggerPlugin.class);
        funcCallbackPlugin = funcPluginClass(FuncLoggerPlugin.class, DefaultFuncLoggerPlugin.class);
        funcLifePlugin = funcPluginClass(FuncLoggerPlugin.class, DefaultFuncLoggerPlugin.class);
        return funcLifePlugin.life(methodSetting.getFuncMethod(), methodSetting.getProxy(), methodSetting.getProxyMethod(), methodSetting.getArgs());
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
    private <T> T funcPluginClass(Class<?> classFile, Class<?> defaultClass) {
        String link = Objects.requireNonNull(format("?:?", methodSetting.getBeanName(), classFile.getSimpleName()));
        T object = methodSetting.getFuncLink().getObject(link);
        if (object == null) {
            try {
                object = (T) defaultClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return object;
    }

}
