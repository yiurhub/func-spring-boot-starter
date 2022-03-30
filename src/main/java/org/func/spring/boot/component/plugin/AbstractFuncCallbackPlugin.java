package org.func.spring.boot.component.plugin;

import org.func.spring.boot.component.callback.FuncCallback;
import org.func.spring.boot.component.callback.FuncCallbackError;
import org.func.spring.boot.component.callback.FuncCallbackThen;
import org.func.spring.boot.component.callback.SimpleFuncCallback;
import org.func.spring.boot.type.FuncToolType;
import org.func.spring.boot.container.FuncMethod;
import org.func.spring.boot.factory.agent.FuncLink;
import org.func.spring.boot.properties.FuncProperties;
import org.func.spring.boot.utils.FuncString;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.func.spring.boot.utils.FuncString.format;

/**
 * @author Yiur
 */
public abstract class AbstractFuncCallbackPlugin implements FuncCallbackPlugin {

    private String beanName;

    private String[] refs;

    private FuncLink funcLink;

    private FuncProperties funcProperties;

    private FuncLoggerPlugin funcLoggerPlugin;

    public AbstractFuncCallbackPlugin() {
    }

    public AbstractFuncCallbackPlugin(String beanName, String[] refs, FuncLink funcLink, FuncProperties funcProperties, FuncLoggerPlugin funcLoggerPlugin) {
        this.beanName = beanName;
        this.refs = refs;
        this.funcLink = funcLink;
        this.funcProperties = funcProperties;
        this.funcLoggerPlugin = funcLoggerPlugin;
    }

    /**
     * func link setObject (bean:callback | bean:callback-XXX) callback resolve
     * extends FuncCallbackPlugin interface rewrite resolve method
     * achieving a custom execution effect
     * <pre>
     *         FuncLife funcLife = funcLink.getObject(FuncLife.class, format(FuncString.FUNC_LINK_FORMAT, beanName, FuncToolType.LIFE_KEY.value));
     *         FuncLifeStart funcLifeStart = funcLink.getObject(FuncLifeStart.class, format(FuncString.FUNC_LINK_FORMAT, beanName, FuncToolType.LIFE_START_KEY.value));
     *         FuncLifeEnd funcLifeEnd = funcLink.getObject(FuncLifeEnd.class, format(FuncString.FUNC_LINK_FORMAT, beanName, FuncToolType.LIFE_END_KEY.value));
     *         if (funcLife == null) {
     *             for (String ref : refs) {
     *                 if (funcLife == null) {
     *                     funcLife = funcLink.getObject(FuncLife.class, format(FuncString.FUNC_LINK_FORMAT, ref, FuncToolType.LIFE_KEY.value));
     *                 }
     *                 if (funcLifeStart == null) {
     *                     funcLifeStart = funcLink.getObject(FuncLifeStart.class, format(FuncString.FUNC_LINK_FORMAT, ref, FuncToolType.LIFE_START_KEY.value));
     *                 }
     *                 if (funcLifeEnd == null) {
     *                     funcLifeEnd = funcLink.getObject(FuncLifeEnd.class, format(FuncString.FUNC_LINK_FORMAT, ref, FuncToolType.LIFE_END_KEY.value));
     *                 }
     *             }
     *         }
     * </pre>
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
            FuncCallback funcCallback = null;
            FuncCallbackThen funcCallbackThen = funcLink.getObject(FuncCallbackThen.class, format(FuncString.FUNC_LINK_FORMAT, beanName, FuncToolType.CALLBACK_THEN_KEY.value));
            FuncCallbackError funcCallbackError = funcLink.getObject(FuncCallbackError.class, format(FuncString.FUNC_LINK_FORMAT, beanName, FuncToolType.CALLBACK_ERROR_KEY.value));
            if (funcLink.getObject(FuncCallback.class, format(FuncString.FUNC_LINK_FORMAT, beanName, FuncToolType.CALLBACK_KEY.value)) != null) {
                funcCallback = funcLink.getObject(FuncCallback.class, format(FuncString.FUNC_LINK_FORMAT, beanName, FuncToolType.CALLBACK_KEY.value));
            } else {
                for (String ref : refs) {
                    if (funcCallback == null) {
                        funcCallback = funcLink.getObject(FuncCallback.class, format(FuncString.FUNC_LINK_FORMAT, ref, FuncToolType.CALLBACK_KEY.value));
                    }
                    if (funcCallbackThen == null) {
                        funcCallbackThen = funcLink.getObject(FuncCallbackThen.class, format(FuncString.FUNC_LINK_FORMAT, ref, FuncToolType.CALLBACK_THEN_KEY.value));
                    }
                    if (funcCallbackError == null) {
                        funcCallbackError = funcLink.getObject(FuncCallbackError.class, format(FuncString.FUNC_LINK_FORMAT, ref, FuncToolType.CALLBACK_ERROR_KEY.value));
                    }
                }
            }

            if (funcCallback == null) {
                if (funcMethod.getCallbackClass() != FuncCallback.class) {
                    funcCallback = (FuncCallback) funcMethod.getCallbackClass().newInstance();
                }
                if (funcProperties.getCallBack() != FuncCallback.class && funcCallback instanceof SimpleFuncCallback) {
                    funcCallback = (FuncCallback) funcProperties.getCallBack().newInstance();
                }
            }

            Object result = null;
            try {
                if (funcCallbackThen != null) {
                    funcMethod.constraint.put(FuncMethod.INVOKE_RESULT, true);
                    result = funcCallbackThen.then(method.invoke(proxy, objects.toArray()));
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                if (funcCallbackError != null) {
                    funcMethod.constraint.put(FuncMethod.INVOKE_RESULT, false);
                    result = funcCallbackError.error(e.getCause());
                }
            }
            try {
                if (funcCallbackThen == null) {
                    funcMethod.constraint.put(FuncMethod.INVOKE_RESULT, true);
                    result = funcCallback.then(method.invoke(proxy, objects.toArray()));
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                if (funcCallbackError == null) {
                    funcMethod.constraint.put(FuncMethod.INVOKE_RESULT, false);
                    result = funcCallback.error(e.getCause());
                }
            }

            parameter.put(format("?:result", beanName), result);
            String resolveLog = funcLoggerPlugin.resolve(funcMethod, parameter);
            if (funcMethod.getLogger().isEnableLog()) {
                funcLoggerPlugin.write(resolveLog, funcMethod);
            }

            return result;
        }

        return method.invoke(proxy, objects.toArray());
    }

    public FuncCallback getCallback(FuncMethod funcMethod) {
        FuncCallback funcCallback = null;
        if (funcMethod.isCallback()) {
            if (funcLink.getObject(FuncCallback.class, format(FuncString.FUNC_LINK_FORMAT, beanName, FuncToolType.CALLBACK_KEY.value)) != null) {
                funcCallback = funcLink.getObject(FuncCallback.class, format(FuncString.FUNC_LINK_FORMAT, beanName, FuncToolType.CALLBACK_KEY.value));
            } else {
                for (String ref : refs) {
                    if (funcCallback == null) {
                        funcCallback = funcLink.getObject(FuncCallback.class, format(FuncString.FUNC_LINK_FORMAT, ref, FuncToolType.CALLBACK_KEY.value));
                    }
                }
            }
        }
        return funcCallback;
    }

    public FuncCallbackThen getCallbackThen(FuncMethod funcMethod) {
        FuncCallbackThen funcCallbackThen = funcLink.getObject(FuncCallbackThen.class, format(FuncString.FUNC_LINK_FORMAT, beanName, FuncToolType.CALLBACK_THEN_KEY.value));
        if (funcMethod.isCallback()) {
            if (funcLink.getObject(FuncCallback.class, format(FuncString.FUNC_LINK_FORMAT, beanName, FuncToolType.CALLBACK_KEY.value)) == null) {
                for (String ref : refs) {
                    if (funcCallbackThen == null) {
                        funcCallbackThen = funcLink.getObject(FuncCallbackThen.class, format(FuncString.FUNC_LINK_FORMAT, ref, FuncToolType.CALLBACK_THEN_KEY.value));
                    }
                }
            }
        }
        return funcCallbackThen;
    }

    public FuncCallbackError getCallbackError(FuncMethod funcMethod) {
        FuncCallbackError funcCallbackError = funcLink.getObject(FuncCallbackError.class, format(FuncString.FUNC_LINK_FORMAT, beanName, FuncToolType.CALLBACK_ERROR_KEY.value));
        if (funcMethod.isCallback()) {
            if (funcLink.getObject(FuncCallback.class, format(FuncString.FUNC_LINK_FORMAT, beanName, FuncToolType.CALLBACK_KEY.value)) == null) {
                for (String ref : refs) {
                    if (funcCallbackError == null) {
                        funcCallbackError = funcLink.getObject(FuncCallbackError.class, format(FuncString.FUNC_LINK_FORMAT, ref, FuncToolType.CALLBACK_ERROR_KEY.value));
                    }
                }
            }
        }
        return funcCallbackError;
    }

}
