package org.func.spring.boot.component.plugin;

import org.func.spring.boot.link.FuncLink;
import org.func.spring.boot.properties.FuncProperties;

/**
 * default achieve FuncLoggerPlugin
 * @author Yiur
 */
public final class DefaultFuncLoggerPlugin extends AbstractFuncLoggerPlugin {

    public DefaultFuncLoggerPlugin() {
    }

    public DefaultFuncLoggerPlugin(String beanName, String alias, String[] refs, FuncLink funcLink, FuncProperties funcProperties) {
        super(beanName, alias, refs, funcLink, funcProperties);
    }

}
