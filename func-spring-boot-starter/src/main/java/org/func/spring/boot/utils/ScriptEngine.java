package org.func.spring.boot.utils;

import org.func.spring.boot.exception.FuncException;

import javax.script.Bindings;
import javax.script.ScriptEngineManager;
import java.util.LinkedHashMap;

/**
 * Anonymous function string expression engine utility class
 * @author Yiur
 */
public final class ScriptEngine {

    /**
     * DEFAULT_ENGINE default parsing engine
     */
    private static final String DEFAULT_ENGINE = "groovy";

    /**
     * Execute string expression to return generic constraint
     * @param expression string expression
     * @param parameter the bound object applies the map collection
     * @param <R> used to constrain the return value
     * @return R
     */
    @SuppressWarnings("unchecked")
    public static <R> R eval(String expression, LinkedHashMap<String, Object> parameter) throws FuncException {
        R result = null;
        ScriptEngineManager engineManager = new ScriptEngineManager();
        javax.script.ScriptEngine scriptEngine = engineManager.getEngineByName(DEFAULT_ENGINE);
        Bindings bindings = bindExpr(scriptEngine, parameter);
        try {
            result = (R) scriptEngine.eval(expression, bindings);
        } catch (Exception e) {
            throw new FuncException("func link logger resolve invoke script error");
        }
        return result;
    }

    /**
     * Used to return a bound string expression reference object
     * @param scriptEngine pass in the generated engine object
     * @param parameter the bound object applies the map collection
     * @return Bindings
     */
    private static Bindings bindExpr(javax.script.ScriptEngine scriptEngine, LinkedHashMap<String, Object> parameter) {
        Bindings bindings = scriptEngine.createBindings();
        bindings.putAll(parameter);
        return bindings;
    }

}
