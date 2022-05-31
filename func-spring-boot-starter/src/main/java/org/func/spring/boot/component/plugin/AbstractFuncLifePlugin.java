package org.func.spring.boot.component.plugin;

import org.func.spring.boot.link.AbstractFuncLink;
import org.func.spring.boot.component.Obtain;
import org.func.spring.boot.component.life.FuncLife;
import org.func.spring.boot.component.life.FuncLifeEnd;
import org.func.spring.boot.component.life.FuncLifeStart;
import org.func.spring.boot.method.FuncMethod;
import org.func.spring.boot.link.FuncLink;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yiur
 */
public abstract class AbstractFuncLifePlugin implements FuncLifePlugin {

    private String beanName;

    private String alias;

    private String[] refs;

    private FuncLink funcLink;

    private FuncCallbackPlugin funcCallbackPlugin;

    public AbstractFuncLifePlugin() {
    }

    public AbstractFuncLifePlugin(String beanName, String alias, String[] refs, FuncLink funcLink, FuncCallbackPlugin funcCallbackPlugin) {
        this.beanName = beanName;
        this.alias = alias;
        this.refs = refs;
        this.funcLink = funcLink;
        this.funcCallbackPlugin = funcCallbackPlugin;
    }

    /**
     * func link setObject (bean:life | bean:life-XXX) life resolve
     * extends FuncLifePlugin interface rewrite resolve method
     * achieving a custom execution effect
     * @param funcMethod func method
     * @param proxy proxy object
     * @param objectMethod current object method
     * @param objects current args
     * @return Object
     * @throws Throwable running
     */
    @Override
    public Object life(FuncMethod funcMethod, Object proxy, Method objectMethod, Object[] objects) throws Throwable {
        Map<String, Object> parameter = getParameter(funcMethod, objects);

        FuncLife funcLife = Obtain.get(funcLink, beanName, refs, AbstractFuncLink.LIFE);
        FuncLifeStart funcLifeStart = Obtain.get(funcLink, beanName, refs, AbstractFuncLink.LIFE_START);
        FuncLifeEnd funcLifeEnd = Obtain.get(funcLink, beanName, refs, AbstractFuncLink.LIFE_END);

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

    protected Map<String, Object> getParameter(FuncMethod funcMethod, Object[] objects) {
        Map<String, Object> parameter = new LinkedHashMap<>();
        List<String> parameterName = funcMethod.getParameterName();
        for (int i = 0; i < parameterName.size(); i++) {
            parameter.put(parameterName.get(i), objects[i]);
        }
        return parameter;
    }

}
