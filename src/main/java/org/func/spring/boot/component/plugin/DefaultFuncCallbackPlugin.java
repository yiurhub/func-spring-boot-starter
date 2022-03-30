package org.func.spring.boot.component.plugin;

import org.func.spring.boot.factory.agent.FuncLink;
import org.func.spring.boot.properties.FuncProperties;

/**
 * default achieve FuncCallbackPlugin
 * @author Yiur
 */
public final class DefaultFuncCallbackPlugin extends AbstractFuncCallbackPlugin {

    public DefaultFuncCallbackPlugin() {
    }

    public DefaultFuncCallbackPlugin(String beanName, String[] refs, FuncLink funcLink, FuncProperties funcProperties, FuncLoggerPlugin funcLoggerPlugin) {
        super(beanName, refs, funcLink, funcProperties, funcLoggerPlugin);
    }

}
