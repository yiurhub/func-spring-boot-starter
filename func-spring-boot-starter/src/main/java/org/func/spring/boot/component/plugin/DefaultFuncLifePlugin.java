package org.func.spring.boot.component.plugin;

import org.func.spring.boot.link.FuncLink;

/**
 * default achieve FuncLifePlugin
 * @author Yiur
 */
public final class DefaultFuncLifePlugin extends AbstractFuncLifePlugin {

    public DefaultFuncLifePlugin() {
    }

    public DefaultFuncLifePlugin(String beanName, String alias, String[] refs, FuncLink funcLink, FuncCallbackPlugin funcCallbackPlugin) {
        super(beanName, alias, refs, funcLink, funcCallbackPlugin);
    }

}
