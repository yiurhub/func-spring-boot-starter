package org.func.spring.boot.component.plugin;

import org.func.spring.boot.component.life.FuncLife;
import org.func.spring.boot.component.life.FuncLifeEnd;
import org.func.spring.boot.component.life.FuncLifeStart;
import org.func.spring.boot.type.FuncToolType;
import org.func.spring.boot.container.FuncMethod;
import org.func.spring.boot.factory.agent.FuncLink;
import org.func.spring.boot.utils.FuncString;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.func.spring.boot.utils.FuncString.format;

/**
 * @author Yiur
 */
public abstract class AbstractFuncLifePlugin implements FuncLifePlugin {

    private String beanName;

    private String[] refs;

    private FuncLink funcLink;

    private FuncCallbackPlugin funcCallbackPlugin;

    public AbstractFuncLifePlugin() {
    }

    public AbstractFuncLifePlugin(String beanName, String[] refs, FuncLink funcLink, FuncCallbackPlugin funcCallbackPlugin) {
        this.beanName = beanName;
        this.refs = refs;
        this.funcLink = funcLink;
        this.funcCallbackPlugin = funcCallbackPlugin;
    }

    /**
     * func link setObject (bean:life | bean:life-XXX) life resolve
     * extends FuncLifePlugin interface rewrite resolve method
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
     * @param objectMethod current object method
     * @param objects current args
     * @return Object
     * @throws Throwable running
     */
    @Override
    public Object life(FuncMethod funcMethod, Object proxy, Method objectMethod, Object[] objects) throws Throwable {
        Map<String, Object> parameter = new LinkedHashMap<>();
        List<String> parameterName = funcMethod.getParameterName();
        for (int i = 0; i < parameterName.size(); i++) {
            parameter.put(parameterName.get(i), objects[i]);
        }

        FuncLife funcLife = funcLink.getObject(FuncLife.class, format(FuncString.FUNC_LINK_FORMAT, beanName, FuncToolType.LIFE_KEY.value));
        FuncLifeStart funcLifeStart = funcLink.getObject(FuncLifeStart.class, format(FuncString.FUNC_LINK_FORMAT, beanName, FuncToolType.LIFE_START_KEY.value));
        FuncLifeEnd funcLifeEnd = funcLink.getObject(FuncLifeEnd.class, format(FuncString.FUNC_LINK_FORMAT, beanName, FuncToolType.LIFE_END_KEY.value));
        if (funcLife == null) {
            for (String ref : refs) {
                if (funcLife == null) {
                    funcLife = funcLink.getObject(FuncLife.class, format(FuncString.FUNC_LINK_FORMAT, ref, FuncToolType.LIFE_KEY.value));
                }
                if (funcLifeStart == null) {
                    funcLifeStart = funcLink.getObject(FuncLifeStart.class, format(FuncString.FUNC_LINK_FORMAT, ref, FuncToolType.LIFE_START_KEY.value));
                }
                if (funcLifeEnd == null) {
                    funcLifeEnd = funcLink.getObject(FuncLifeEnd.class, format(FuncString.FUNC_LINK_FORMAT, ref, FuncToolType.LIFE_END_KEY.value));
                }
            }
        }

        if (funcLife != null) {
            return funcLife.end(funcCallbackPlugin.resolve(funcMethod, proxy, objectMethod, funcLife.start(parameter)));
        }

        if (funcLifeStart != null) {
            parameter = funcLifeStart.start(parameter);
        }

        if (funcLifeEnd != null) {
            return funcLifeEnd.end(funcCallbackPlugin.resolve(funcMethod, proxy, objectMethod, parameter));
        }

        return funcCallbackPlugin.resolve(funcMethod, proxy, objectMethod, parameter);
    }

}
