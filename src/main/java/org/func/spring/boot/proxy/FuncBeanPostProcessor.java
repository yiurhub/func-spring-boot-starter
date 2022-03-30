package org.func.spring.boot.proxy;

import org.func.spring.boot.annotation.FuncBean;
import org.func.spring.boot.annotation.FuncLambda;
import org.func.spring.boot.proxy.handel.FuncLinkBeanHandler;
import org.func.spring.boot.proxy.init.FuncInitBeanRegistrar;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.*;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;

import java.util.*;

/**
 * Register anonymous function bean
 * @author Yiur
 */
public class FuncBeanPostProcessor implements EnvironmentAware, ApplicationContextAware, BeanDefinitionRegistryPostProcessor {

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
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);

        provider.addIncludeFilter((metadataReader, metadataReaderFactory) -> {
            AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();

            Set<String> targets = annotationMetadata.getAnnotationTypes();
            if (targets.contains(FuncBean.class.getName())) {
                parseFuncMethod(registry, annotationMetadata);
            }
            return false;
        });
        provider.findCandidateComponents(FuncInitBeanRegistrar.packages);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

    /**
     * parse anonymous function information
     * @param registry bean definition registry
     * @param annotationMetadata current function annotation metadata
     */
    private void parseFuncMethod(BeanDefinitionRegistry registry, AnnotationMetadata annotationMetadata) {
        Set<MethodMetadata> annotatedMethods = annotationMetadata.getAnnotatedMethods(FuncLambda.class.getName());
        FuncLinkBeanHandler funcBeanHandler = new FuncLinkBeanHandler(context, registry);
        for (MethodMetadata methodMetadata : annotatedMethods) {
            String beanName = funcBeanHandler.beanName(methodMetadata);
        }
    }

}
