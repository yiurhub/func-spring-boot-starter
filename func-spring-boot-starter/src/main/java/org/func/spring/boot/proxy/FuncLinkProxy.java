package org.func.spring.boot.proxy;

import org.func.spring.boot.annotation.FuncBean;
import org.func.spring.boot.component.plugin.FuncInvokePlugin;
import org.func.spring.boot.method.FuncMethod;
import org.func.spring.boot.properties.FuncProperties;
import org.func.spring.boot.pool.FuncStringConstantPool;
import org.func.spring.boot.exception.FuncFinalException;
import org.func.spring.boot.utils.StringUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

import static org.func.spring.boot.utils.StringUtil.matcher;

/**
 * @author Yiur
 */
public class FuncLinkProxy<T> implements InvocationHandler, Serializable {

    private static final long serialVersionUID = 476835913369828380L;

    private final List<FuncMethod> funcMethods;

    private final Class<T> classObject;

    private final FuncInvokePlugin funcInvokePlugin;

    public FuncLinkProxy(ApplicationContext context, Class<T> classObject, String beanName, String alias, String[] refs, List<FuncMethod> funcMethods) {
        this.classObject = classObject;
        this.funcMethods = funcMethods;

        FuncProperties funcProperties = context.getBean(FuncProperties.class);
        org.func.spring.boot.link.FuncLink funcLink;
        try {
            funcLink = context.getBean(org.func.spring.boot.link.FuncLink.class);
        } catch (BeansException e) {
            String funcLinkBean = this.classObject.getAnnotationsByType(FuncBean.class)[0].value();
            if (!funcLinkBean.equals(StringUtil.EMPTY)) {
                funcLink = (org.func.spring.boot.link.FuncLink) context.getBean(funcLinkBean);
            } else {
                funcLink = new org.func.spring.boot.link.FuncLink(funcProperties);
            }
        }
        for (FuncMethod funcMethod : funcMethods) {
            FuncProperties.Logger logger = funcMethod.getLogger();
            FuncProperties.Logger.setLogger(logger, funcProperties);
        }

        funcInvokePlugin = new FuncInvokePlugin(beanName, alias, refs, funcLink, funcProperties);
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
                FuncMethod funcMethod = getFuncMethod(getMethodName(method));
                Method objectMethod = classObject.getMethod(method.getName(), method.getParameterTypes());

                funcInvokePlugin.setArgs(args);
                funcInvokePlugin.setFuncMethod(funcMethod);
                funcInvokePlugin.setObjectMethod(objectMethod);
                return funcInvokePlugin.invoke();
            }
        } catch (Throwable t) {
            throw FuncFinalException.unwrapThrowable(t);
        }
    }

    private String getMethodName(Method method) {
        String currentMethodSource = method.toGenericString();
        if (!matcher(FuncStringConstantPool.STRING_ARRAY_CHAR, currentMethodSource).equals(FuncStringConstantPool.EMPTY)) {
            currentMethodSource = currentMethodSource.replace(FuncStringConstantPool.STRING_ARRAY_CHAR, FuncStringConstantPool.STRING_ARRAY_TYPE);
        }
        return currentMethodSource;
    }

    private FuncMethod getFuncMethod(String currentMethodSource) {
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
