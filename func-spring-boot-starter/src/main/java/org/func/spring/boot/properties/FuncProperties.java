package org.func.spring.boot.properties;

import lombok.Data;
import org.func.spring.boot.component.callback.FuncCallback;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static org.func.spring.boot.utils.FuncString.*;

/**
 * Anonymous function configuration
 * @author Yiur
 */
@Data
@ConfigurationProperties(prefix = "func-link")
public class FuncProperties {

    /**
     * enable log
     */
    private Logger logger;
    /**
     * Enable lambda expressions
     */
    private boolean lambda = true;
    /**
     * Enable singleton mode
     */
    private boolean isSingleton = true;
    /**
     * Bind the callback function class
     */
    private Class<? extends FuncCallback> callBack = FuncCallback.class;

    public FuncProperties() {
    }

    /**
     * Anonymous function log configuration
     */
    @Data
    public static class Logger {

        /**
         * enable log
         */
        private boolean enableLog = false;
        /**
         * log output path
         */
        private String path = formatTranslate("?/log", System.getProperty("user.dir"));
        /**
         * global log prefix
         */
        private String fileName = "func-link";
        /**
         * global log suffix
         */
        private String fileSuffix = "log";
        /**
         * date formatting
         */
        private String dateFormat = "yyyy-MM-dd HH:mm:ss:SSS";
        /**
         * output log information
         */
        private String message = "[${invokeResult}] [${dateTime}] call method --> ${methodName}(${parameterSource})\r\n";

        public Logger() {
        }

        /**
         * set logger attribute
         * @param logger logger attribute
         * @param funcProperties func properties
         */
        public static void setLogger(Logger logger, FuncProperties funcProperties) {
            if (!logger.isEnableLog()) {
                logger.setEnableLog(funcProperties.getLogger().isEnableLog());
            }
            if (logger.getPath() == null) {
                logger.setPath(funcProperties.getLogger().getPath());
            }
            if (logger.getFileName() == null) {
                logger.setFileName(funcProperties.getLogger().getFileName());
            }
            if (logger.getFileSuffix() == null) {
                logger.setFileSuffix(funcProperties.getLogger().getFileSuffix());
            }
            if (logger.getMessage() == null) {
                logger.setMessage(funcProperties.getLogger().getMessage());
            }
            if (logger.getDateFormat() == null) {
                logger.setDateFormat(funcProperties.getLogger().getDateFormat());
            }
        }

    }

}
