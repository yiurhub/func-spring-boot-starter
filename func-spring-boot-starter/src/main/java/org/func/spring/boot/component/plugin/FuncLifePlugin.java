package org.func.spring.boot.component.plugin;

import org.func.spring.boot.method.FuncMethod;

import java.lang.reflect.Method;

/**
 * funcLink method life plugin
 * <pre>
 *     funcLink
 *                 .&lt;FuncLifeStart&gt;setObject("bean:life-start", args -&gt; args)
 *                 .&lt;FuncLifeEnd&lt;Object, Object&gt;&gt;setObject("bean:life-end", data -&gt; data)
 * </pre>
 * @author Yiur
 */
public interface FuncLifePlugin extends FuncLinkPlugin {

    /**
     * func link setObject (bean:life | bean:life-XXX) life resolve
     * extends FuncLifePlugin interface rewrite resolve method
     * achieving a custom execution effect
     * @param funcMethod func method
     * @param proxy proxy object
     * @param objectMethod current object method
     * @param objects current args
     * @return Object
     * @throws Throwable running
     */
    Object life(FuncMethod funcMethod, Object proxy, Method objectMethod, Object[] objects) throws Throwable;

}
