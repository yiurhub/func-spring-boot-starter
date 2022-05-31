package org.func.spring.boot.component.plugin;

import org.func.spring.boot.method.FuncMethod;
import org.func.spring.boot.exception.FuncLoggerException;
import org.func.spring.boot.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.func.spring.boot.utils.StringUtil.format;

/**
 * funcLink method logger plugin
 * <pre>
 *     funcLink
 *                 .&lt;FuncLogger&gt;setObject("bean:log", (set, param) -&gt; "write log"))
 * </pre>
 * @author Yiur
 */
public interface FuncLoggerPlugin extends FuncLinkPlugin {

    /**
     * func link setObject (bean:log) log resolve
     * extends FuncLoggerPlugin interface rewrite resolve method
     * achieving a custom execution effect
     * @param funcMethod func method
     * @param parameter current parameter
     * @param result call method return value
     * @return String
     */
    String resolve(FuncMethod funcMethod, Map<String, Object> parameter, Object result) throws Throwable;

    /**
     * default achieving log write method
     * @param log log info
     * @param funcMethod func method
     * @throws IOException running
     */
    default void write(String log, FuncMethod funcMethod) throws Throwable {
        String path = funcMethod.getLogger().getPath();
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String name = funcMethod.getLogger().getFileName();
        String suffix = funcMethod.getLogger().getFileSuffix();
        String filePath = format("?/?.?", path, name, suffix);

        try {
            FileUtil.write(funcMethod, filePath, log);
        } catch (IOException e) {
            throw new FuncLoggerException("Anonymous function log output error!");
        }
    }

}
