package org.func.spring.boot.component.plugin;

import org.func.spring.boot.container.FuncMethod;

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
     * <pre>
     *         FuncLife funcLife = funcLink.getObject(FuncLife.class, format(FuncString.FUNC_LINK_FORMAT, beanName, FuncToolType.LIFE_KEY.value));
     *         FuncLifeStart funcLifeStart = funcLink.getObject(FuncLifeStart.class, format(FuncString.FUNC_LINK_FORMAT, beanName, FuncToolType.LIFE_START_KEY.value));
     *         FuncLifeEnd funcLifeEnd = funcLink.getObject(FuncLifeEnd.class, format(FuncString.FUNC_LINK_FORMAT, beanName, FuncToolType.LIFE_END_KEY.value));
     *         if (funcLife == null) {
     *             for (String ref : refs) {
     *                 if (funcLife == null) {
     *                     funcLife = funcLink.getObject(FuncLife.class, format(FuncString.FUNC_LINK_FORMAT, ref, FuncToolType.LIFE_KEY.value));
     *                 }
     *                 if (funcLifeStart == null) {
     *                     funcLifeStart = funcLink.getObject(FuncLifeStart.class, format(FuncString.FUNC_LINK_FORMAT, ref, FuncToolType.LIFE_START_KEY.value));
     *                 }
     *                 if (funcLifeEnd == null) {
     *                     funcLifeEnd = funcLink.getObject(FuncLifeEnd.class, format(FuncString.FUNC_LINK_FORMAT, ref, FuncToolType.LIFE_END_KEY.value));
     *                 }
     *             }
     *         }
     * </pre>
     * @param funcMethod func method
     * @param proxy proxy object
     * @param objectMethod current object method
     * @param objects current args
     * @return Object
     * @throws Throwable running
     */
    Object life(FuncMethod funcMethod, Object proxy, Method objectMethod, Object[] objects) throws Throwable;

}
