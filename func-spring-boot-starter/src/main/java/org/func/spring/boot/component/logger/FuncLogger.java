package org.func.spring.boot.component.logger;

import org.func.spring.boot.component.FuncLinkObject;
import org.func.spring.boot.exception.FuncLoggerException;

import java.util.Set;

/**
 * Anonymous function configuration log
 * @author Yiur
 */
public interface FuncLogger extends FuncLinkObject {

    /**
     * output log
     * @param keys Anonymous function information collection
     * @return String
     * @exception FuncLoggerException t
     */
    String logger(Set<String> keys) throws FuncLoggerException;

}
