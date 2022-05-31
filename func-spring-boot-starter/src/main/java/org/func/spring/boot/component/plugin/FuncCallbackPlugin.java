package org.func.spring.boot.component.plugin;

import org.func.spring.boot.method.FuncMethod;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * funcLink method callback plugin
 * <pre>
 *     funcLink
 *                 .&lt;FuncCallbackThen&lt;Object, Object&gt;&gt;setObject("bean:callback-then", result -&gt; result)
 *                 .&lt;FuncCallbackError&lt;Object&gt;&gt;setObject("bean:callback-error", exception -&gt; exception);
 * </pre>
 * @author Yiur
 */
public interface FuncCallbackPlugin extends FuncLinkPlugin {

    /**
     * func link setObject (bean:callback | bean:callback-XXX) callback resolve
     * extends FuncCallbackPlugin interface rewrite resolve method
     * achieving a custom execution effect
     * @param funcMethod func method
     * @param proxy proxy object
     * @param method current method
     * @param parameter current invoke parameter
     * @return Object
     * @throws Throwable running
     */
    Object resolve(FuncMethod funcMethod, Object proxy, Method method, Map<String, Object> parameter) throws Throwable;

}
