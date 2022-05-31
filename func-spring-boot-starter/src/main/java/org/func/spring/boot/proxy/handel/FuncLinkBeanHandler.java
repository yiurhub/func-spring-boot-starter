package org.func.spring.boot.proxy.handel;

import org.func.spring.boot.annotation.FuncAlias;
import org.func.spring.boot.annotation.FuncLambda;
import org.func.spring.boot.annotation.FuncRefs;
import org.func.spring.boot.proxy.FuncLinkFactoryBean;
import org.func.spring.boot.utils.ClassScanner;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.reflect.Method;
import java.util.*;

import static org.func.spring.boot.pool.FuncStringConstantPool.EMPTY;
import static org.func.spring.boot.utils.StringUtil.lowercaseFirstLetter;

/**
 * @author Yiur
 */
public final class FuncLinkBeanHandler {

    private final static String CONTEXT = "context";
    private final static String BEAN_NAME = "beanName";
    private final static String ALIAS = "alias";
    private final static String REFS = "refs";
    private final static String CLASS_OBJECT = "classObject";

    private final ApplicationContext context;

    private final BeanDefinitionRegistry registry;

    public FuncLinkBeanHandler(ApplicationContext context, BeanDefinitionRegistry registry) {
        this.context = context;
        this.registry = registry;
    }

    public void compile(Method method) {
        FuncLambda funcLambda = ClassScanner.getAnnotation(FuncLambda.class, method);
        FuncAlias funcAlias = ClassScanner.getAnnotation(FuncAlias.class, method);
        FuncRefs funcRefs = ClassScanner.getAnnotation(FuncRefs.class, method);
        Class<?> interfaceType = loadClass(((Class<?>) compare(funcLambda.value(), funcLambda.classFile())).getName());
        Class<?> classObject = method.getDeclaringClass();
        String beanName = checkBeanName(funcLambda.bean(), interfaceType, classObject);
        String alias = funcAlias != null ? funcAlias.value() : funcLambda.alias();
        String[] refs = funcRefs != null ? funcRefs.value() : funcLambda.refs();
        try {
            BeanDefinition bean = registry.getBeanDefinition(beanName);
            if (Objects.equals(bean.getBeanClassName(), FuncLinkFactoryBean.class.getName())) {
                return;
            }
            beanDefinition(interfaceType, classObject.getName(), beanName, alias, refs);
        } catch (NoSuchBeanDefinitionException e) {
            beanDefinition(interfaceType, classObject.getName(), beanName, alias, refs);
        }
    }

    private Class<?> loadClass(String className) {
        Class<?> loadClass = null;
        try {
            loadClass = Objects.requireNonNull(((DefaultListableBeanFactory) registry).getBeanClassLoader()).loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return loadClass;
    }

    private String checkBeanName(String beanName, Class<?> interfaceType, Class<?> classObject) {
        String checkBeanName = beanName;
        if (checkBeanName.equals(EMPTY)) {
            for (Class<?> anInterface : classObject.getInterfaces()) {
                if (anInterface.getName().equals(interfaceType.getName())) {
                    checkBeanName = lowercaseFirstLetter(classObject.getSimpleName());
                }
            }
            if (checkBeanName.equals(EMPTY)) {
                checkBeanName = lowercaseFirstLetter(interfaceType.getSimpleName());
            }
        }
        return checkBeanName;
    }

    private static Object compare(Object... args) {
        for (Object arg : args) {
            if (arg != Object.class) {
                return arg;
            }
        }
        return null;
    }

    public void beanDefinition(Class<?> interfaceType, String classObject, String beanName, String alias, String[] refs) {
        ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext) context).getBeanFactory();
        GenericBeanDefinition beanDefinition;
        try {
            beanDefinition = (GenericBeanDefinition) beanFactory.getBeanDefinition(beanName);
            funcBeanDefinitionInit(beanDefinition, interfaceType, classObject, beanName, alias, refs, AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        } catch (NoSuchBeanDefinitionException e) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(interfaceType);
            beanDefinition = (GenericBeanDefinition) builder.getRawBeanDefinition();
            funcBeanDefinitionInit(beanDefinition, interfaceType, classObject, beanName, alias, refs, AbstractBeanDefinition.AUTOWIRE_BY_NAME);
            registry.registerBeanDefinition(beanName, beanDefinition);
        }
    }

    private void funcBeanDefinitionInit(GenericBeanDefinition beanDefinition, Class<?> interfaceType, String classObject, String beanName, String alias, String[] refs, int autowireMode) {
        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(interfaceType);
        beanDefinition.getPropertyValues()
                .add(BEAN_NAME, beanName)
                .add(ALIAS, alias)
                .add(CONTEXT, context)
                .add(REFS, refs)
                .add(CLASS_OBJECT, classObject);
        beanDefinition.setLazyInit(true);
        beanDefinition.setBeanClass(FuncLinkFactoryBean.class);
        beanDefinition.setAutowireMode(autowireMode);
    }

}
