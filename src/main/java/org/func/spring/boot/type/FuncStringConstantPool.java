package org.func.spring.boot.type;

/**
 * anonymous function string constant pool
 * @author Yiur
 */
public interface FuncStringConstantPool {

    /**
     * EMPTY
     */
    String EMPTY = "";
    /**
     * STRING_LETTER
     */
    String STRING_LETTER = "[A-z][A-z]*";
    /**
     * STRING_FORMAT
     */
    String STRING_FORMAT = "\\?";
    /**
     * STRING_REPLACE
     */
    String STRING_REPLACE = "[$][{][\\W\\w]*?[}]";
    /**
     * STRING_ARRAY_CHAR
     */
    String STRING_ARRAY_CHAR = "...";
    /**
     * STRING_ARRAY_TYPE
     */
    String STRING_ARRAY_TYPE = "[]";
    /**
     * FUNC_LINK_FORMAT
     */
    String FUNC_LINK_FORMAT = "?:?";

}
