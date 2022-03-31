package com.simple.web.plugin;

import org.func.spring.boot.component.plugin.AbstractFuncCallbackPlugin;
import org.func.spring.boot.component.plugin.FuncLoggerPlugin;
import org.func.spring.boot.factory.agent.FuncLink;
import org.func.spring.boot.properties.FuncProperties;

/**
 * @author Yiur
 */
public class SimpleCallbackPlugin extends AbstractFuncCallbackPlugin {

    public SimpleCallbackPlugin() {
    }

    public SimpleCallbackPlugin(String beanName, String[] refs, FuncLink funcLink, FuncProperties funcProperties, FuncLoggerPlugin funcLoggerPlugin) {
        super(beanName, refs, funcLink, funcProperties, funcLoggerPlugin);
    }

}
