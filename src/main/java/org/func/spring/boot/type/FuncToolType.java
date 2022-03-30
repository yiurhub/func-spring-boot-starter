package org.func.spring.boot.type;

/**
 * Anonymous function state attribute
 * @author Yiur
 */
public enum FuncToolType {

    /**
     * LIFE_KEY
     */
    LIFE_KEY("life"),
    /**
     * LIFE_START_KEY
     */
    LIFE_START_KEY("life-start"),
    /**
     * LIFE_END_KEY
     */
    LIFE_END_KEY("life-end"),
    /**
     * CALLBACK_KEY
     */
    CALLBACK_KEY("callback"),
    /**
     * CALLBACK_KEY
     */
    CALLBACK_THEN_KEY("callback-then"),
    /**
     * CALLBACK_KEY
     */
    CALLBACK_ERROR_KEY("callback-error"),
    /**
     * LOGGER_KEY
     */
    LOGGER_KEY("log");

    /**
     * this value
     */
    public String value;

    FuncToolType(String value) {
        this.value = value;
    }

}
