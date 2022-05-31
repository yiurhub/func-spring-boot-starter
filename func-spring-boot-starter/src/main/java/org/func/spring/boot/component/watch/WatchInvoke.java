package org.func.spring.boot.component.watch;

import org.func.spring.boot.link.AbstractFuncLink;
import org.func.spring.boot.component.Obtain;
import org.func.spring.boot.exception.FuncWatchException;
import org.func.spring.boot.filter.ParseFilter;
import org.func.spring.boot.link.FuncLink;

import java.lang.reflect.Method;

import static org.func.spring.boot.pool.FuncStringConstantPool.EMPTY;

/**
 * @author Yiur
 */
public final class WatchInvoke {

    private final static int MAX_WATCH_METHOD = 1;

    private final String bean;

    private final FuncWatch funcWatch;

    public WatchInvoke(String bean, FuncWatch funcWatch) {
        this.bean = bean;
        this.funcWatch = funcWatch;
    }

    public static WatchInvoke get(FuncLink funcLink, String beanName, String[] refs, String alias) {
        FuncWatch funcWatch = Obtain.get(funcLink, beanName, refs, AbstractFuncLink.WATCH);
        if (refs.length > 1 && alias.equals(EMPTY)) {
            return new WatchInvoke(refs[0], funcWatch);
        }
        return null;
    }

    public Object defaultInvoke(Object... args) throws Throwable {
        if (funcWatch.getClass().getDeclaredMethods().length > MAX_WATCH_METHOD) {
            throw new FuncWatchException("This listening lambda expression can only have one watch method");
        }
        Method declaredMethod = funcWatch.getClass().getDeclaredMethods()[0];
        declaredMethod.setAccessible(true);
        return declaredMethod.invoke(funcWatch, args);
    }

    public Object invokeByAnnotation(ParseFilter filter) throws Throwable {
        return filter.parse(funcWatch.getClass().getDeclaredMethods());
    }

    public Object invoke(String bean, Object... args) throws Throwable {
        for (Method declaredMethod : funcWatch.getClass().getDeclaredMethods()) {
            declaredMethod.setAccessible(true);
            if (declaredMethod.getName().equals(bean)) {
                return declaredMethod.invoke(funcWatch, args);
            }
        }
        return null;
    }

    public String getBean() {
        return bean;
    }

    public FuncWatch getFuncWatch() {
        return funcWatch;
    }

}