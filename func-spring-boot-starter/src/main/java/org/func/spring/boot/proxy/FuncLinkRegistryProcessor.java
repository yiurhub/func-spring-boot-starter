package org.func.spring.boot.proxy;

import org.func.spring.boot.proxy.init.FuncInitBeanRegistrar;
import org.func.spring.boot.utils.ClassScanner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.*;
import org.springframework.core.env.Environment;

/**
 * Register anonymous function bean
 * @author Yiur
 */
public class FuncLinkRegistryProcessor implements EnvironmentAware, ApplicationContextAware, BeanDefinitionRegistryPostProcessor {

    private Environment environment;

    private ApplicationContext context;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        ClassScanner funcClassScanner = new ClassScanner();
        funcClassScanner.setPackages(FuncInitBeanRegistrar.packages);
        funcClassScanner.setEnvironment(environment);
        funcClassScanner.setRegistry(registry);
        funcClassScanner.setContext(context);
        funcClassScanner.processing();
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

}
