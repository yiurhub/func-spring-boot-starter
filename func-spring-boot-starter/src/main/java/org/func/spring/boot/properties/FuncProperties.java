package org.func.spring.boot.properties;

import org.func.spring.boot.component.callback.FuncCallback;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static org.func.spring.boot.utils.StringUtil.*;

/**
 * Anonymous function configuration
 * @author Yiur
 */
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
        logger = new Logger();
    }

    /**
     * Anonymous function log configuration
     */
    public static class Logger {

        /**
         * enable log
         */
        private boolean enableLog = false;
        /**
         * log output path
         */
        private String path;
        /**
         * global log prefix
         */
        private String fileName;
        /**
         * global log suffix
         */
        private String fileSuffix;
        /**
         * date formatting
         */
        private String dateFormat;
        /**
         * output log information
         */
        private String message;

        public Logger() {
            this.path = formatTranslate("?/log", System.getProperty("user.dir"));
            this.fileName = "func-link";
            this.fileSuffix = "log";
            this.dateFormat = "yyyy-MM-dd HH:mm:ss:SSS";
            this.message = "[${invokeResult}] [${dateTime}] call method --> ${methodName}(${parameterSource})\r\n";
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

        public boolean isEnableLog() {
            return enableLog;
        }

        public void setEnableLog(boolean enableLog) {
            this.enableLog = enableLog;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileSuffix() {
            return fileSuffix;
        }

        public void setFileSuffix(String fileSuffix) {
            this.fileSuffix = fileSuffix;
        }

        public String getDateFormat() {
            return dateFormat;
        }

        public void setDateFormat(String dateFormat) {
            this.dateFormat = dateFormat;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public boolean isLambda() {
        return lambda;
    }

    public void setLambda(boolean lambda) {
        this.lambda = lambda;
    }

    public boolean isSingleton() {
        return isSingleton;
    }

    public void setSingleton(boolean singleton) {
        isSingleton = singleton;
    }

    public Class<? extends FuncCallback> getCallBack() {
        return callBack;
    }

    public void setCallBack(Class<? extends FuncCallback> callBack) {
        this.callBack = callBack;
    }

}
