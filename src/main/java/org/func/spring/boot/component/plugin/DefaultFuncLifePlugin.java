package org.func.spring.boot.component.plugin;

import org.func.spring.boot.factory.agent.FuncLink;

/**
 * default achieve FuncLifePlugin
 * @author Yiur
 */
public final class DefaultFuncLifePlugin extends AbstractFuncLifePlugin {

    public DefaultFuncLifePlugin() {
    }

    public DefaultFuncLifePlugin(String beanName, String[] refs, FuncLink funcLink, FuncCallbackPlugin funcCallbackPlugin) {
        super(beanName, refs, funcLink, funcCallbackPlugin);
    }

}
