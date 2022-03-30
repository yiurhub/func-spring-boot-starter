package org.func.spring.boot.type;

/**
 * Anonymous function properties
 * @author Yiur
 */
public enum FuncLambdaType {

    /**
     * CLASS_FILE
     */
    CLASS_FILE("classFile"),
    /**
     * BEAN
     */
    BEAN("bean"),
    /**
     * CALLBACK
     */
    CALLBACK("callback"),
    /**
     * CALLBACK_CLASS
     */
    CALLBACK_CLASS("value"),
    /**
     * REFS
     */
    REFS("refs");

    /**
     * this value
     */
    public String value;

    FuncLambdaType(String value) {
        this.value = value;
    }

}
