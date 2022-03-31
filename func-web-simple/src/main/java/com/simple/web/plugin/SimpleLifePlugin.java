package com.simple.web.plugin;

import org.func.spring.boot.component.plugin.AbstractFuncLifePlugin;
import org.func.spring.boot.component.plugin.FuncCallbackPlugin;
import org.func.spring.boot.factory.agent.FuncLink;

/**
 * @author Yiur
 */
public class SimpleLifePlugin extends AbstractFuncLifePlugin {

    public SimpleLifePlugin() {
    }

    public SimpleLifePlugin(String beanName, String[] refs, FuncLink funcLink, FuncCallbackPlugin funcCallbackPlugin) {
        super(beanName, refs, funcLink, funcCallbackPlugin);
    }

}
