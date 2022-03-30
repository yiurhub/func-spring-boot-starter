package org.func.spring.boot.proxy;

import lombok.extern.slf4j.Slf4j;
import org.func.spring.boot.annotation.FuncBean;
import org.func.spring.boot.component.plugin.DefaultFuncInvokePlugin;
import org.func.spring.boot.container.FuncMethod;
import org.func.spring.boot.factory.agent.FuncLink;
import org.func.spring.boot.properties.FuncProperties;
import org.func.spring.boot.type.FuncStringConstantPool;
import org.func.spring.boot.utils.ExceptionUtil;
import org.func.spring.boot.utils.FuncString;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

import static org.func.spring.boot.utils.FuncString.format;
import static org.func.spring.boot.utils.FuncString.matcher;

/**
 * @author Yiur
 */
@Slf4j
public class FuncLinkProxy<T> implements InvocationHandler, Serializable {

    private static final long serialVersionUID = 476835913369828380L;

    private final ApplicationContext context;

    private final String beanName;

    private final List<FuncMethod> funcMethods;

    private final Class<T> classObject;

    private final DefaultFuncInvokePlugin funcInvokePlugin;

    /**
     * bind the properties of the proxy object
     * @param classObject current class object
     * @param beanName current bean name
     * @param refs referenced bean name
     * @param funcMethods current proxy func link bean methods
     */
    public FuncLinkProxy(Class<T> classObject, ApplicationContext context, String beanName, String[] refs, List<FuncMethod> funcMethods) {
        // Get class attributes and bean factories
        this.classObject = classObject;
        this.context = context;
        this.beanName = beanName;
        this.funcMethods = funcMethods;

        // Get configuration for anonymous functions and anonymous function chaining
        FuncProperties funcProperties = this.context.getBean(FuncProperties.class);
        FuncLink funcLink;
        try {
            funcLink = this.context.getBean(FuncLink.class);
        } catch (BeansException e) {
            String funcLinkBean = this.classObject.getAnnotationsByType(FuncBean.class)[0].link();
            if (!funcLinkBean.equals(FuncString.EMPTY)) {
                funcLink = (FuncLink) this.context.getBean(funcLinkBean);
            } else {
                funcLink = new FuncLink(funcProperties);
            }
        }
        for (FuncMethod funcMethod : funcMethods) {
            FuncProperties.Logger logger = funcMethod.getLogger();
            FuncProperties.Logger.setLogger(logger, funcProperties);
            log.info(format("?->?(?) logger path: ? file:?.?", classObject.getSimpleName(), funcMethod.getMethodName(), funcMethod.getParameterSource(), logger.getPath(), logger.getFileName(), logger.getFileSuffix()));
        }

        funcInvokePlugin = new DefaultFuncInvokePlugin(beanName, refs, funcLink, funcProperties);
        T bean = context.getAutowireCapableBeanFactory().createBean(classObject);
        context.getAutowireCapableBeanFactory().autowireBean(bean);
        funcInvokePlugin.setProxy(bean);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(this, args);
            } else {
                FuncMethod funcMethod = funcMethod(methodName(method));
                Method objectMethod = classObject.getMethod(method.getName(), method.getParameterTypes());

                funcInvokePlugin.setArgs(args);
                funcInvokePlugin.setFuncMethod(funcMethod);
                funcInvokePlugin.setObjectMethod(objectMethod);
                return funcInvokePlugin.invoke();
            }
        } catch (Throwable t) {
            throw ExceptionUtil.unwrapThrowable(t);
        }
    }

    /**
     * bean exits
     * @return boolean
     */
    private boolean beanExits() {
        try {
            context.getBean(beanName);
            return true;
        } catch (BeansException e) {
            return false;
        }
    }

    /**
     * get current method source
     * @param method current method
     * @return String
     */
    private String methodName(Method method) {
        String currentMethodSource = method.toGenericString();
        if (!matcher(FuncStringConstantPool.STRING_ARRAY_CHAR, currentMethodSource).equals(FuncStringConstantPool.EMPTY)) {
            currentMethodSource = currentMethodSource.replace(FuncStringConstantPool.STRING_ARRAY_CHAR, FuncStringConstantPool.STRING_ARRAY_TYPE);
        }
        return currentMethodSource;
    }

    /**
     * get current func method
     * @param currentMethodSource current method source
     * @return FuncMethod
     */
    private FuncMethod funcMethod(String currentMethodSource) {
        FuncMethod funcMethod = null;
        for (FuncMethod beanMethod : funcMethods) {
            if (matcher(FuncMethod.REGEX_SOURCE, currentMethodSource).equals(beanMethod.getSource())) {
                funcMethod = beanMethod;
                break;
            }
        }
        return funcMethod;
    }

}
