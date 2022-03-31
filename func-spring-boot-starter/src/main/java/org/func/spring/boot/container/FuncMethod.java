package org.func.spring.boot.container;

import lombok.Data;
import org.func.spring.boot.annotation.FuncCallback;
import org.func.spring.boot.annotation.FuncLambda;
import org.func.spring.boot.annotation.FuncLogger;
import org.func.spring.boot.annotation.FuncParameter;
import org.func.spring.boot.properties.FuncProperties;
import org.func.spring.boot.type.FuncLambdaType;
import org.func.spring.boot.type.FuncLogType;
import org.func.spring.boot.type.FuncStringConstantPool;
import org.func.spring.boot.utils.FuncString;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.type.MethodMetadata;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

import static org.func.spring.boot.utils.FuncString.*;

/**
 * Anonymous method attribute
 * @author Yiur
 */
@Data
public class FuncMethod implements FuncConstantPool {

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

    public FuncMethod(MethodMetadata methodMetadata, List<FuncMethod> funcMethods) {
        try {
            MergedAnnotations annotations = methodMetadata.getAnnotations();
            // Get the log information of anonymous function binding
            MergedAnnotation<FuncLogger> funcLogger = annotations.get(FuncLogger.class);
            // Get the binding class information of an anonymous function
            MergedAnnotation<FuncLambda> funcLambda = annotations.get(FuncLambda.class);
            // Get the callback function class bound by the anonymous function
            MergedAnnotation<FuncCallback> bindCallback = annotations.get(FuncCallback.class);

            Class<?> classObject = Class.forName(methodMetadata.getDeclaringClassName());
            for (Method method : classObject.getDeclaredMethods()) {
                Method currentMethod = getMethod(classObject, funcMethods, methodMetadata, method);

                if (currentMethod != null && currentMethod.getAnnotation(FuncLambda.class).bean().equals(annotations.get(FuncLambda.class).getString(FuncLambdaType.BEAN.value))) {
                    // set logger attribute
                    setLogger(funcLogger);
                    // set anonymous function name
                    setMethodName(methodMetadata.getMethodName());
                    // set return type
                    setResultType(method.getReturnType());
                    // set parameter type
                    setParameterName(parseParameterType(currentMethod));
                    // String to set the parameter type
                    setParameterSource(charLinkFormat(getParameterName(), ", "));
                    // String to set the anonymous function
                    setSource(matcher(REGEX_SOURCE, methodMetadata.toString()));
                    // whether to enable the callback function
                    setCallback(funcLambda.getBoolean(FuncLambdaType.CALLBACK.value));
                    // Callback function default action
                    setCallbackClass(bindCallback.isPresent() ? bindCallback.getClass(FuncLambdaType.CALLBACK_CLASS.value) : (Class<?>) FuncCallback.class.getDeclaredMethods()[0].getDefaultValue());
                    // add constraint
                    addConstraint();
                    return;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the currently written anonymous function information
     * @param classObject Managed anonymous function class
     * @param funcMethods store the parsed anonymous function collection
     * @param methodMetadata Metadata for anonymous functions
     * @param method current method information
     * @return Method
     */
    private Method getMethod(Class<?> classObject, List<FuncMethod> funcMethods, MethodMetadata methodMetadata, Method method) {
        Method currentMethod = null;
        try {
            if (!existFuncMethod(funcMethods, methodMetadata, method)) {
                try {
                    String parameterType = FuncString.removeParentheses(matcher(PARAMETER_TYPE_REGEX, matcher(REGEX_SOURCE, methodMetadata.toString())));
                    if (!parameterType.equals(FuncString.EMPTY)) {
                        throw new NoSuchMethodException();
                    }
                    currentMethod = classObject.getMethod(methodMetadata.getMethodName());
                } catch (NoSuchMethodException e) {
                    currentMethod = classObject.getMethod(methodMetadata.getMethodName(), method.getParameterTypes());
                }
            }
        } catch (Exception ignored) {
        }
        return currentMethod;
    }

    /**
     * is there a parsing method
     * @param funcMethods store the parsed anonymous function collection
     * @param methodMetadata  Metadata for anonymous functions
     * @param method current method information
     * @return boolean
     */
    private boolean existFuncMethod(List<FuncMethod> funcMethods, MethodMetadata methodMetadata, Method method) {
        boolean exist = false;
        String funcLinkMethod = matcher(REGEX_SOURCE, methodMetadata.toString());
        String linkMethod = matcher(REGEX_SOURCE, method.toString());
        for (FuncMethod funcMethod : funcMethods) {
            if (linkMethod.equals(funcMethod.getSource())) {
                exist = true;
            }
        }
        if (!funcLinkMethod.equals(linkMethod)) {
            exist = true;
        }
        return exist;
    }

    /**
     * parse the type of a field property
     * @param method current method information
     * @return List
     */
    private List<String> parseParameterType(Method method) {
        setParameterType(method.getParameterTypes());
        List<String> parameters = new ArrayList<>();
        for (Parameter parameter : method.getParameters()) {
            Assert.notNull(parameter.getAnnotation(FuncParameter.class), FuncString.format("The parameter (?) of the current anonymous function (?) cannot be declared without @FuncParameter annotation", getMethodName(), parameter.getName()));
            parameters.add(parameter.getAnnotation(FuncParameter.class).value());
        }
        return parameters;
    }

    /**
     * set logger attribute
     * @param funcLogger logger annotation
     */
    private void setLogger(MergedAnnotation<FuncLogger> funcLogger) {
        // Open log
        logger.setDateFormat(null);
        logger.setFileName(null);
        logger.setFileSuffix(null);
        logger.setMessage(null);
        logger.setPath(null);
        logger.setEnableLog(funcLogger.isPresent());
        if (funcLogger.isPresent()) {
            // set log properties
            if (!funcLogger.getString(FuncLogType.PATH.value).equals(FuncStringConstantPool.EMPTY)) {
                logger.setPath(funcLogger.getString(FuncLogType.PATH.value));
            }
            if (!funcLogger.getString(FuncLogType.NAME.value).equals(FuncStringConstantPool.EMPTY)) {
                logger.setFileName(funcLogger.getString(FuncLogType.NAME.value));
            }
            if (!funcLogger.getString(FuncLogType.SUFFIX.value).equals(FuncStringConstantPool.EMPTY)) {
                logger.setFileSuffix(funcLogger.getString(FuncLogType.SUFFIX.value));
            }
        }
    }

}
