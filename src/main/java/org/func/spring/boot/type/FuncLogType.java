package org.func.spring.boot.type;

/**
 * Anonymous function log information
 * @author Yiur
 */
public enum FuncLogType {

    /**
     * PATH
     */
    PATH("path"),
    /**
     * NAME
     */
    NAME("name"),
    /**
     * SUFFIX
     */
    SUFFIX("suffix");

    /**
     * this value
     */
    public String value;

    FuncLogType(String value) {
        this.value = value;
    }

}
