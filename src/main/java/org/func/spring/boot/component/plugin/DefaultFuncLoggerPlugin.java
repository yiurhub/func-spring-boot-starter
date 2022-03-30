package org.func.spring.boot.component.plugin;

import org.func.spring.boot.factory.agent.FuncLink;
import org.func.spring.boot.properties.FuncProperties;

/**
 * default achieve FuncLoggerPlugin
 * @author Yiur
 */
public final class DefaultFuncLoggerPlugin extends AbstractFuncLoggerPlugin {

    public DefaultFuncLoggerPlugin() {
    }

    public DefaultFuncLoggerPlugin(String beanName, String[] refs, FuncLink funcLink, FuncProperties funcProperties) {
        super(beanName, refs, funcLink, funcProperties);
    }

}
