package org.func.spring.boot.component.plugin;

import org.func.spring.boot.link.FuncLink;
import org.func.spring.boot.properties.FuncProperties;

/**
 * default achieve FuncCallbackPlugin
 * @author Yiur
 */
public final class DefaultFuncCallbackPlugin extends AbstractFuncCallbackPlugin {

    public DefaultFuncCallbackPlugin() {
    }

    public DefaultFuncCallbackPlugin(String beanName, String alias, String[] refs, FuncLink funcLink, FuncProperties funcProperties, FuncLoggerPlugin funcLoggerPlugin) {
        super(beanName, alias, refs, funcLink, funcProperties, funcLoggerPlugin);
    }

}
