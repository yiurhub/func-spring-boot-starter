package org.func.spring.boot.utils;

import org.func.spring.boot.annotation.FuncBean;
import org.func.spring.boot.proxy.handel.FuncLinkBeanHandler;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author Yiur
 */
public final class ClassScanner {

    private ApplicationContext context;

    private Environment environment;

    private BeanDefinitionRegistry registry;

    private String packages;

    private ClassPathScanningCandidateComponentProvider provider;

    private FuncLinkBeanHandler funcLinkBeanHandler;

    public ClassScanner() {
        this.provider = new ClassPathScanningCandidateComponentProvider(false);
    }

    public void processing() {
        this.funcLinkBeanHandler = new FuncLinkBeanHandler(context, registry);
        provider.addIncludeFilter((metadataReader, metadataReaderFactory) -> {
            AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();

            Set<String> targets = annotationMetadata.getAnnotationTypes();
            if (targets.contains(FuncBean.class.getName())) {
                initBean(annotationMetadata.getClassName());
            }
            return false;
        });
        provider.findCandidateComponents(this.packages);
    }

    private void initBean(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            for (Method method : getMethods(clazz)) {
                funcLinkBeanHandler.compile(method);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Method[] getMethods(Class<?> classes) {
        Method[] declaredMethods = classes.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            declaredMethod.setAccessible(true);
        }
        return declaredMethods;
    }

    public static <T extends Annotation> T getClassAnnotation(Class<T> tClass, Class<?> classes) {
        return classes.getAnnotation(tClass);
    }

    public static <T extends Annotation> T getMethodAnnotation(Class<T> tClass, Method method) {
        return method.getAnnotation(tClass);
    }

    public static <T extends Annotation> T getAnnotation(Class<T> tClass, Method method) {
        T annotation = method.getAnnotation(tClass);
        if (annotation == null) {
            annotation = method.getDeclaringClass().getAnnotation(tClass);
        }
        return annotation;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public void setRegistry(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

}
