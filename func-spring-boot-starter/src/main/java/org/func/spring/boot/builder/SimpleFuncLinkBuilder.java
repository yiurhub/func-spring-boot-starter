package org.func.spring.boot.builder;

import org.func.spring.boot.link.FuncLink;
import org.func.spring.boot.properties.FuncProperties;

/**
 * @author Yiur
 */
public class SimpleFuncLinkBuilder implements FuncLinkBuilder {

    private final FuncProperties funcProperties;

    public SimpleFuncLinkBuilder(FuncProperties funcProperties) {
        this.funcProperties = funcProperties;
    }

    @Override
    public FuncLink create() {
        return new FuncLink(funcProperties);
    }

}
