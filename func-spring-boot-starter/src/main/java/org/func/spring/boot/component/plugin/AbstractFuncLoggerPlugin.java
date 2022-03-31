package org.func.spring.boot.component.plugin;

import org.func.spring.boot.component.logger.FuncLogger;
import org.func.spring.boot.type.FuncToolType;
import org.func.spring.boot.container.FuncMethod;
import org.func.spring.boot.factory.agent.FuncLink;
import org.func.spring.boot.properties.FuncProperties;
import org.func.spring.boot.utils.FuncString;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static org.func.spring.boot.utils.FuncString.format;

/**
 * @author Yiur
 */
public abstract class AbstractFuncLoggerPlugin implements FuncLoggerPlugin {

    private String beanName;

    private String[] refs;

    private FuncLink funcLink;

    private FuncProperties funcProperties;

    public AbstractFuncLoggerPlugin() {
    }

    public AbstractFuncLoggerPlugin(String beanName, String[] refs, FuncLink funcLink, FuncProperties funcProperties) {
        this.beanName = beanName;
        this.refs = refs;
        this.funcLink = funcLink;
        this.funcProperties = funcProperties;
    }

    /**
     * func link setObject (bean:log) log resolve
     * extends FuncLoggerPlugin interface rewrite resolve method
     * achieving a custom execution effect
     * <pre>
     *             FuncLogger funcLogger = funcLink.getObject(FuncLogger.class, format(FuncString.FUNC_LINK_FORMAT, beanName, FuncToolType.LOGGER_KEY.value));
     *             if (funcLogger == null) {
     *                 for (String ref : refs) {
     *                     funcLogger = funcLink.getObject(FuncLogger.class, format(FuncString.FUNC_LINK_FORMAT, ref, FuncToolType.LOGGER_KEY.value));
     *                     if (funcLogger != null) {
     *                         break;
     *                     }
     *                 }
     *             }
     * </pre>
     * @param funcMethod func method
     * @param parameter current parameter
     * @return String
     */
    @Override
    public String resolve(FuncMethod funcMethod, Map<String, Object> parameter) {
        if (funcMethod.getLogger().isEnableLog() || funcProperties.getLogger().isEnableLog()) {
            FuncLogger funcLogger = funcLink.getObject(FuncLogger.class, format(FuncString.FUNC_LINK_FORMAT, beanName, FuncToolType.LOGGER_KEY.value));
            if (funcLogger == null) {
                for (String ref : refs) {
                    funcLogger = funcLink.getObject(FuncLogger.class, format(FuncString.FUNC_LINK_FORMAT, ref, FuncToolType.LOGGER_KEY.value));
                    if (funcLogger != null) {
                        break;
                    }
                }
            }

            funcMethod.constraint.put(FuncMethod.DATE_TIME, new SimpleDateFormat(funcProperties.getLogger().getDateFormat()).format(new Date()));

            if (funcLogger != null) {
                return funcLogger.logger(funcMethod.constraint.keySet(), parameter);
            }
        }
        return funcProperties.getLogger().getMessage();
    }

}
