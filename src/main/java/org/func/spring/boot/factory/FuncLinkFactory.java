package org.func.spring.boot.factory;

import org.func.spring.boot.factory.agent.FuncLink;
import org.func.spring.boot.properties.FuncProperties;

/**
 * @author Yiur
 */
public class FuncLinkFactory {

    private FuncProperties funcProperties;

    public FuncLinkFactory(FuncProperties funcProperties) {
        this.funcProperties = funcProperties;
    }

    public FuncProperties getFuncProperties() {
        return funcProperties;
    }

    public void setFuncProperties(FuncProperties funcProperties) {
        this.funcProperties = funcProperties;
    }

    public FuncLink build() {
        return new FuncLink(funcProperties);
    }

}
