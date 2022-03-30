package org.func.spring.boot.type;

/**
 * Anonymous function open attribute
 * @author Yiur
 */
public enum EnableFuncLambdaType {

    /**
     * VALUE
     */
    VALUE("value"),
    /**
     * PACKAGES
     */
    PACKAGES("packages");

    /**
     * this value
     */
    public String value;

    EnableFuncLambdaType(String value) {
        this.value = value;
    }

}
