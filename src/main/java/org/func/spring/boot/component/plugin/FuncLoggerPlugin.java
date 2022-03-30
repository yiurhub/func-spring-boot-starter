package org.func.spring.boot.component.plugin;

import org.func.spring.boot.container.FuncMethod;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import static org.func.spring.boot.utils.FuncString.format;
import static org.func.spring.boot.utils.FuncString.replace;

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
    String resolve(FuncMethod funcMethod, Map<String, Object> parameter);

    /**
     * default achieving log write method
     * @param log log info
     * @param funcMethod func method
     * @throws IOException running
     */
    default void write(String log, FuncMethod funcMethod) throws IOException {
        String path = funcMethod.getLogger().getPath();
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String name = funcMethod.getLogger().getFileName();
        String suffix = funcMethod.getLogger().getFileSuffix();
        String filePath = format("?/?.?", path, name, suffix);

        FileWriter writer = new FileWriter(new File(filePath), true);
        writer.write(replace(log, funcMethod.constraint));
        writer.flush();
        writer.close();
    }

}
