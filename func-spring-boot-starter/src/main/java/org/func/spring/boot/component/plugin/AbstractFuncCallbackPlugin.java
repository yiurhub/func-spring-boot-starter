package org.func.spring.boot.component.plugin;

import org.func.spring.boot.link.AbstractFuncLink;
import org.func.spring.boot.component.Obtain;
import org.func.spring.boot.component.callback.FuncCallback;
import org.func.spring.boot.component.callback.FuncCallbackError;
import org.func.spring.boot.component.callback.FuncCallbackThen;
import org.func.spring.boot.component.callback.SimpleFuncCallback;
import org.func.spring.boot.component.watch.WatchInvoke;
import org.func.spring.boot.method.FuncMethod;
import org.func.spring.boot.link.FuncLink;
import org.func.spring.boot.properties.FuncProperties;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Yiur
 */
public abstract class AbstractFuncCallbackPlugin implements FuncCallbackPlugin {

    private String beanName;

    private String alias;

    private String[] refs;

    private FuncLink funcLink;

    private FuncProperties funcProperties;

    private FuncLoggerPlugin funcLoggerPlugin;

    public AbstractFuncCallbackPlugin() {
    }

    public AbstractFuncCallbackPlugin(String beanName, String alias, String[] refs, FuncLink funcLink, FuncProperties funcProperties, FuncLoggerPlugin funcLoggerPlugin) {
        this.beanName = beanName;
        this.alias = alias;
        this.refs = refs;
        this.funcLink = funcLink;
        this.funcProperties = funcProperties;
        this.funcLoggerPlugin = funcLoggerPlugin;
    }

    /**
     * func link setObject (bean:callback | bean:callback-XXX) callback resolve
     * extends FuncCallbackPlugin interface rewrite resolve method
     * achieving a custom execution effect
     * @param funcMethod func method
     * @param proxy proxy object
     * @param method current method
     * @param parameter current invoke parameter
     * @return Object
     * @throws Throwable running
     */
    @Override
    public Object resolve(FuncMethod funcMethod, Object proxy, Method method, Map<String, Object> parameter) throws Throwable {
        List<Object> objects = new ArrayList<>();
        for (String key : parameter.keySet()) {
            objects.add(parameter.get(key));
        }

        if (funcMethod.isCallback()) {
            FuncCallback funcCallback = Obtain.get(funcLink, beanName, refs, AbstractFuncLink.CALLBACK);
            FuncCallbackThen funcCallbackThen = Obtain.get(funcLink, beanName, refs, AbstractFuncLink.CALLBACK_THEN);
            FuncCallbackError funcCallbackError = Obtain.get(funcLink, beanName, refs, AbstractFuncLink.CALLBACK_ERROR);
            WatchInvoke watchInvoke = WatchInvoke.get(funcLink, beanName, refs, alias);

            if (funcCallback == null) {
                if (funcMethod.getCallbackClass() != FuncCallback.class) {
                    funcCallback = (FuncCallback) funcMethod.getCallbackClass().newInstance();
                }
                if (funcProperties.getCallBack() != FuncCallback.class && funcCallback instanceof SimpleFuncCallback) {
                    funcCallback = funcProperties.getCallBack().newInstance();
                }
            }

            CallbackInvoke callbackInvoke = new CallbackInvoke(funcCallback, funcCallbackThen, funcCallbackError, funcMethod);
            Object result = callbackInvoke.invoke(proxy, method, objects.toArray());

            String resolve = funcLoggerPlugin.resolve(funcMethod, parameter, result);
            funcMethod.getLogger().setMessage(resolve);
            if (funcMethod.getLogger().isEnableLog()) {
                funcLoggerPlugin.write(funcMethod.getLogger().getMessage(), funcMethod);
            }
            return result;
        }

        return method.invoke(proxy, objects.toArray());
    }

    protected static class CallbackInvoke {

        private final FuncCallback funcCallback;

        private final FuncCallbackThen funcCallbackThen;

        private final FuncCallbackError funcCallbackError;

        private final FuncMethod funcMethod;

        public CallbackInvoke(FuncCallback funcCallback, FuncCallbackThen funcCallbackThen, FuncCallbackError funcCallbackError, FuncMethod funcMethod) {
            this.funcCallback = funcCallback;
            this.funcCallbackThen = funcCallbackThen;
            this.funcCallbackError = funcCallbackError;
            this.funcMethod = funcMethod;
        }

        public Object invoke(Object proxy, Method method, Object[] objects) {
            return callback(proxy, method, objects);
        }

        public Object callback(Object proxy, Method method, Object[] objects) {
            try {
                Object invoke = method.invoke(proxy, objects);
                if (funcCallbackThen == null) {
                    funcMethod.constraint.put(FuncMethod.INVOKE_RESULT, true);
                    return funcCallback.then(invoke);
                } else {
                    funcMethod.constraint.put(FuncMethod.INVOKE_RESULT, true);
                    return funcCallbackThen.then(invoke);
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                if (funcCallbackError == null) {
                    funcMethod.constraint.put(FuncMethod.INVOKE_RESULT, false);
                    return funcCallback.error(e.getCause());
                } else {
                    funcMethod.constraint.put(FuncMethod.INVOKE_RESULT, false);
                    return funcCallbackError.error(e.getCause());
                }
            }
        }

    }

}
