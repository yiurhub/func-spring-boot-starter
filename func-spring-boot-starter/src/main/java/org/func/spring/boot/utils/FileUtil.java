package org.func.spring.boot.utils;

import org.func.spring.boot.method.FuncMethod;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Yiur
 */
public final class FileUtil {

    public static void write(FuncMethod funcMethod, String filePath, String log) throws IOException {
        FileWriter writer = new FileWriter(new File(filePath), true);
        writer.write(log);
        writer.flush();
    }

}
