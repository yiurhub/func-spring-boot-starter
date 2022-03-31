package org.func.spring.boot.component.logger;

import java.util.Map;
import java.util.Set;

/**
 * Anonymous function configuration log
 * @author Yiur
 */
public interface FuncLogger {

    /**
     * output log
     * @param keys Anonymous function information collection
     * @param parameter Parameters before method execution
     * @return String
     */
    String logger(Set<String> keys, Map<String, Object> parameter);

}
