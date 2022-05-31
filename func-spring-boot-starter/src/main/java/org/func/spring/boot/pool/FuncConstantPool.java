package org.func.spring.boot.pool;

/**
 * your function container constant pool
 * @author Yiur
 */
public interface FuncConstantPool {

    /**
     * resolution rules
     */
    String REGEX_SOURCE = "[A-z]*[\\(][\\W\\w]*[\\)]";
    /**
     * resolution rules
     */
    String PARAMETER_TYPE_REGEX = "[(][\\W\\w]*[)]";
    /**
     * method source
     */
    String SOURCE = "source";
    /**
     * method name
     */
    String METHOD_NAME = "methodName";
    /**
     * return type
     */
    String RESULT_TYPE = "resultType";
    /**
     * parameter types
     */
    String PARAMETER_TYPE = "parameterType";
    /**
     * parameter names
     */
    String PARAMETER_NAME = "parameterName";
    /**
     * parameter source
     */
    String PARAMETER_SOURCE = "parameterSource";
    /**
     * enable callback
     */
    String CALLBACK = "callback";
    /**
     * callback function binding class
     */
    String CALLBACK_CLASS = "callbackClass";
    /**
     * dynamic execution time
     */
    String DATE_TIME = "dateTime";
    /**
     * invoke result
     */
    String INVOKE_RESULT = "invokeResult";
    /**
     * logger
     */
    String LOGGER = "logger";

    /**
     * Added method log expression to allow getting the value
     */
    void addConstraint();

}
