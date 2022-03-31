package com.simple.web.plugin;

import org.func.spring.boot.component.plugin.AbstractFuncLoggerPlugin;
import org.func.spring.boot.factory.agent.FuncLink;
import org.func.spring.boot.properties.FuncProperties;

/**
 * @author Yiur
 */
public class SimpleLoggerPlugin extends AbstractFuncLoggerPlugin {

    public SimpleLoggerPlugin() {
    }

    public SimpleLoggerPlugin(String beanName, String[] refs, FuncLink funcLink, FuncProperties funcProperties) {
        super(beanName, refs, funcLink, funcProperties);
    }

}
