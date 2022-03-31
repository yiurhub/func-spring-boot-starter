package org.func.spring.boot.component.life;

/**
 * Call the function after the anonymous function starts
 * @author Yiur
 */
public interface FuncLifeEnd<T, R> {

    /**
     * call function
     * @param result callback function return value
     * @return R
     */
    R end(T result);

}
