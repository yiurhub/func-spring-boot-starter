package org.func.spring.boot;

import org.func.spring.boot.factory.FuncLinkFactory;
import org.func.spring.boot.properties.FuncProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Anonymous function autowiring
 * @author Yiur
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({FuncProperties.class})
public class FuncAutoConfiguration {

    /**
     * FuncFactory
     */
    private final FuncLinkFactory funcLinkFactory;

    public FuncAutoConfiguration(FuncProperties funcProperties) {
        this.funcLinkFactory = new FuncLinkFactory(funcProperties);
    }

    @Bean
    public FuncLinkFactory funcFactory() {
        return funcLinkFactory;
    }

}
