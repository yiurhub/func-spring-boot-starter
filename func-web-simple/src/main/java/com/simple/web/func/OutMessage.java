package com.simple.web.func;

import org.springframework.stereotype.Component;

/**
 * @author Yiur
 */
@Component
public interface OutMessage {

    /**
     * 输出
     * @param message text
     * @param args replace
     * @return string
     */
    String out(String message, String... args);

    /**
     * 输出
     * @param message text
     * @return string
     */
    String out(String message);

}
