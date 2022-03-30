package org.func.spring.boot.proxy.init;

import org.func.spring.boot.annotation.EnableFuncLambda;
import org.func.spring.boot.type.EnableFuncLambdaType;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;
import java.util.Objects;

/**
 * initialization processing
 * @author Yiur
 */
public class FuncInitBeanRegistrar implements ImportBeanDefinitionRegistrar {

    public static String packages;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> annotationAttributes = annotationMetadata.getAnnotationAttributes(EnableFuncLambda.class.getName());
        packages = (String) Objects.requireNonNull(annotationAttributes).get(EnableFuncLambdaType.VALUE.value);
    }

}
