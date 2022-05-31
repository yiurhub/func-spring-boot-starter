package org.func.spring.boot.component.plugin;

import org.func.spring.boot.link.AbstractFuncLink;
import org.func.spring.boot.component.Obtain;
import org.func.spring.boot.component.logger.FuncLogger;
import org.func.spring.boot.component.watch.WatchInvoke;
import org.func.spring.boot.method.FuncMethod;
import org.func.spring.boot.filter.ParseFilter;
import org.func.spring.boot.filter.ParseLogFilter;
import org.func.spring.boot.link.FuncLink;
import org.func.spring.boot.properties.FuncProperties;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static org.func.spring.boot.utils.StringUtil.replace;

/**
 * @author Yiur
 */
public abstract class AbstractFuncLoggerPlugin implements FuncLoggerPlugin {

    private String beanName;

    private String alias;

    private String[] refs;

    private FuncLink funcLink;

    private FuncProperties funcProperties;

    public AbstractFuncLoggerPlugin() {
    }

    public AbstractFuncLoggerPlugin(String beanName, String alias, String[] refs, FuncLink funcLink, FuncProperties funcProperties) {
        this.beanName = beanName;
        this.alias = alias;
        this.refs = refs;
        this.funcLink = funcLink;
        this.funcProperties = funcProperties;
    }

    /**
     * func link setObject (bean:log) log resolve
     * extends FuncLoggerPlugin interface rewrite resolve method
     * achieving a custom execution effect
     * @param funcMethod func method
     * @param parameter current parameter
     * @param result call method return value
     * @return String
     */
    @Override
    public String resolve(FuncMethod funcMethod, Map<String, Object> parameter, Object result) throws Throwable {
        if (funcMethod.getLogger().isEnableLog() || funcProperties.getLogger().isEnableLog()) {
            FuncLogger funcLogger = Obtain.get(funcLink, beanName, refs, AbstractFuncLink.LOG);
            funcMethod.constraint.put(FuncMethod.DATE_TIME, new SimpleDateFormat(funcProperties.getLogger().getDateFormat()).format(new Date()));
            if (funcLogger != null) {
                String replace = replace(funcLogger.logger(funcMethod.constraint.keySet()), funcMethod.constraint);
                WatchInvoke funcWatch = WatchInvoke.get(funcLink, beanName, refs, alias);
                ParseFilter parseFilter = new ParseLogFilter(funcWatch, beanName, refs, replace, parameter, result);
                return (String) funcWatch.invokeByAnnotation(parseFilter);
            }
        }
        return replace(funcProperties.getLogger().getMessage(), funcMethod.constraint);
    }

}
