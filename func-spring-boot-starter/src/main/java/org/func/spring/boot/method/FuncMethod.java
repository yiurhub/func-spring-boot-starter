package org.func.spring.boot.method;

import org.func.spring.boot.annotation.FuncCallback;
import org.func.spring.boot.annotation.FuncLambda;
import org.func.spring.boot.annotation.FuncLogger;
import org.func.spring.boot.annotation.FuncParameter;
import org.func.spring.boot.pool.FuncConstantPool;
import org.func.spring.boot.properties.FuncProperties;
import org.func.spring.boot.pool.FuncStringConstantPool;
import org.func.spring.boot.utils.ClassScanner;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

import static org.func.spring.boot.utils.StringUtil.*;

/**
 * Anonymous method attribute
 * @author Yiur
 */
public class FuncMethod implements Serializable, FuncConstantPool {

    private static final long serialVersionUID = 3880045800337971245L;

    /**
     * fields that the log allows to return
     */
    public final Map<String, Object> constraint = new LinkedHashMap<>();

    private String source;

    private String methodName;

    private Object resultType;

    private Class<?>[] parameterType;

    private List<String> parameterName;

    private String parameterSource;

    private boolean callback;

    private Class<?> callbackClass;

    private final FuncProperties.Logger logger = new FuncProperties.Logger();

    /**
     * Added method log expression to allow getting the value
     */
    @Override
    public void addConstraint() {
        constraint.put(METHOD_NAME, methodName);
        constraint.put(RESULT_TYPE, resultType);
        constraint.put(PARAMETER_NAME, parameterName);
        constraint.put(PARAMETER_SOURCE, parameterSource);
        constraint.put(SOURCE, source);
        constraint.put(CALLBACK, callback);
        constraint.put(CALLBACK_CLASS, callbackClass);
        constraint.put(DATE_TIME, new Date());
        constraint.put(INVOKE_RESULT, false);
        constraint.put(LOGGER, logger);
    }

    public FuncMethod(String bean, String[] refs, Method method) {
        FuncLogger funcLogger = ClassScanner.getAnnotation(FuncLogger.class, method);
        FuncLambda funcLambda = ClassScanner.getAnnotation(FuncLambda.class, method);
        FuncCallback bindCallback = ClassScanner.getAnnotation(FuncCallback.class, method);

        String defaultBeanName = lowercaseFirstLetter(funcLambda.classFile().getSimpleName());
        String defaultImplBeanName = lowercaseFirstLetter(method.getDeclaringClass().getSimpleName());

        if (!bean.equals(defaultBeanName) && !bean.equals(defaultImplBeanName)) {
            return;
        }

        setLogger(funcLogger);
        setMethodName(method.getName());
        setResultType(method.getReturnType());
        setParameterName(parseParameterType(method));
        setParameterSource(charLinkFormat(getParameterName(), ", "));
        setSource(matcher(REGEX_SOURCE, method.toGenericString()));
        setCallback(funcLambda.callback());
        setCallbackClass(bindCallback != null ? bindCallback.value() : (Class<?>) FuncCallback.class.getDeclaredMethods()[0].getDefaultValue());
        addConstraint();
    }

    private List<String> parseParameterType(Method method) {
        setParameterType(method.getParameterTypes());
        List<String> parameters = new ArrayList<>();
        for (Parameter parameter : method.getParameters()) {
            if (parameter.getAnnotation(FuncParameter.class) != null) {
                parameters.add(parameter.getAnnotation(FuncParameter.class).value());
            } else {
                parameters.add(parameter.getName());
            }
        }
        return parameters;
    }

    private void setLogger(FuncLogger funcLogger) {
        logger.setDateFormat(null);
        logger.setFileName(null);
        logger.setFileSuffix(null);
        logger.setMessage(null);
        logger.setPath(null);
        if (funcLogger != null) {
            logger.setEnableLog(funcLogger.enable());
            if (funcLogger.enable()) {
                if (!funcLogger.path().equals(FuncStringConstantPool.EMPTY)) {
                    logger.setPath(funcLogger.path());
                }
                if (!funcLogger.name().equals(FuncStringConstantPool.EMPTY)) {
                    logger.setFileName(funcLogger.name());
                }
                if (!funcLogger.suffix().equals(FuncStringConstantPool.EMPTY)) {
                    logger.setFileSuffix(funcLogger.suffix());
                }
            }
        }
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object getResultType() {
        return resultType;
    }

    public void setResultType(Object resultType) {
        this.resultType = resultType;
    }

    public Class<?>[] getParameterType() {
        return parameterType;
    }

    public void setParameterType(Class<?>[] parameterType) {
        this.parameterType = parameterType;
    }

    public List<String> getParameterName() {
        return parameterName;
    }

    public void setParameterName(List<String> parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterSource() {
        return parameterSource;
    }

    public void setParameterSource(String parameterSource) {
        this.parameterSource = parameterSource;
    }

    public boolean isCallback() {
        return callback;
    }

    public void setCallback(boolean callback) {
        this.callback = callback;
    }

    public Class<?> getCallbackClass() {
        return callbackClass;
    }

    public void setCallbackClass(Class<?> callbackClass) {
        this.callbackClass = callbackClass;
    }

    public FuncProperties.Logger getLogger() {
        return logger;
    }

}
