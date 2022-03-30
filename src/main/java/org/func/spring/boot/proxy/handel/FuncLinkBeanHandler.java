package org.func.spring.boot.proxy.handel;

import org.func.spring.boot.annotation.FuncLambda;
import org.func.spring.boot.container.FuncMethod;
import org.func.spring.boot.type.FuncLambdaType;
import org.func.spring.boot.utils.FuncString;
import org.func.spring.boot.proxy.FuncLinkProxyFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.type.MethodMetadata;

import java.util.*;

/**
 * @author Yiur
 */
public final class FuncLinkBeanHandler {

    private final static String CONTEXT = "context";
    private final static String BEAN_NAME = "beanName";
    private final static String REFS = "refs";
    private final static String CLASS_OBJECT = "classObject";
    private final static String FUNC_METHODS = "funcMethods";

    private final ApplicationContext context;

    private final BeanDefinitionRegistry registry;

    public FuncLinkBeanHandler(ApplicationContext context, BeanDefinitionRegistry registry) {
        this.context = context;
        this.registry = registry;
    }

    /**
     * Get bean based on annotation
     * @param methodMetadata metadata
     * @return String
     */
    public String beanName(MethodMetadata methodMetadata) {
        Map<String, Object> attributes = new HashMap<>(Objects.requireNonNull(methodMetadata.getAnnotationAttributes(FuncLambda.class.getName())));

        String beanName = get(FuncLambdaType.BEAN.value, attributes);
        String[] refs = get(FuncLambdaType.REFS.value, attributes);
        if (beanName.equals(FuncString.EMPTY)) {
            String[] split = methodMetadata.getDeclaringClassName().split("\\.");
            beanName = FuncString.lowercaseFirstLetter(split[split.length - 1]);
        }

        try {
            String bean = registry.getBeanDefinition(beanName).getBeanClassName();
            if (bean != null && bean.equals(FuncLinkProxyFactory.class.getName())) {
                List<FuncMethod> funcMethods = get(FUNC_METHODS, registry.getBeanDefinition(beanName));
                if (funcMethods != null) {
                    funcMethods.add(new FuncMethod(methodMetadata, funcMethods));
                }
                return beanName;
            }
            List<FuncMethod> funcMethods = new ArrayList<>();
            funcMethods.add(new FuncMethod(methodMetadata, funcMethods));
            beanDefinition((Class<?>) attributes.get(FuncLambdaType.CLASS_FILE.value), methodMetadata.getDeclaringClassName(), beanName, refs, funcMethods);
            return beanName;
        } catch (NoSuchBeanDefinitionException e) {
            List<FuncMethod> funcMethods = new ArrayList<>();
            funcMethods.add(new FuncMethod(methodMetadata, funcMethods));
            beanDefinition((Class<?>) attributes.get(FuncLambdaType.CLASS_FILE.value), methodMetadata.getDeclaringClassName(), beanName, refs, funcMethods);
            return beanName;
        }
    }

    /**
     * bean initialization definition
     * @param interfaceType proxy interface
     * @param classObject class object
     * @param beanName spring bean name
     * @param refs func link bind spring boot bean
     * @param funcMethods funcLink proxy method
     */
    public void beanDefinition(Class<?> interfaceType, String classObject, String beanName, String[] refs, List<FuncMethod> funcMethods) {
        ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext) context).getBeanFactory();
        GenericBeanDefinition beanDefinition = null;
        try {
            beanDefinition = (GenericBeanDefinition) beanFactory.getBeanDefinition(beanName);
            funcBeanDefinitionInit(beanDefinition, interfaceType, classObject, beanName, refs, funcMethods, AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        } catch (NoSuchBeanDefinitionException e) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(interfaceType);
            beanDefinition = (GenericBeanDefinition) builder.getRawBeanDefinition();
            funcBeanDefinitionInit(beanDefinition, interfaceType, classObject, beanName, refs, funcMethods, AbstractBeanDefinition.AUTOWIRE_BY_NAME);
            registry.registerBeanDefinition(beanName, beanDefinition);
        }
    }

    /**
     * bean initialization definition
     * @param beanDefinition bean definition
     * @param interfaceType proxy interface
     * @param classObject class object
     * @param beanName spring bean name
     * @param refs func link bind spring boot bean
     * @param funcMethods funcLink proxy method
     * @param autowireMode set autowire mode
     */
    private void funcBeanDefinitionInit(GenericBeanDefinition beanDefinition, Class<?> interfaceType, String classObject, String beanName, String[] refs, List<FuncMethod> funcMethods, int autowireMode) {
        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(interfaceType);
        MutablePropertyValues propertyValues = getPropertyValues(beanDefinition);
        propertyValues
                .add(BEAN_NAME, beanName)
                .add(CONTEXT, context)
                .add(REFS, refs)
                .add(CLASS_OBJECT, classObject)
                .add(FUNC_METHODS, funcMethods);
        beanDefinition.setLazyInit(true);
        beanDefinition.setBeanClass(FuncLinkProxyFactory.class);
        beanDefinition.setAutowireMode(autowireMode);
    }

    private MutablePropertyValues getPropertyValues(BeanDefinition beanDefinition) {
        return beanDefinition.getPropertyValues();
    }

    @SuppressWarnings("unchecked")
    private <T> T get(String key, BeanDefinition beanDefinition) {
        return (T) getPropertyValues(beanDefinition).get(key);
    }

    @SuppressWarnings("unchecked")
    private <T> T get(String key, Map<String, Object> attributes) {
        return (T) attributes.get(key);
    }

}
