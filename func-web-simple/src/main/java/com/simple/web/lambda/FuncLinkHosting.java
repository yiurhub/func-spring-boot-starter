package com.simple.web.lambda;

import com.simple.web.callback.DefaultCallback;
import com.simple.web.func.HttpLink;
import com.simple.web.func.OutMessage;
import org.func.spring.boot.annotation.*;
import org.func.spring.boot.utils.FuncString;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yiur
 */
@FuncBean(link = "simple")
public class FuncLinkHosting implements HttpLink {

    @FuncLogger
    @FuncLambda(classFile = OutMessage.class, refs = { "logger" })
    public String out(@FuncParameter("message") String message, @FuncParameter("args") String... args) {
        return FuncString.format(message, args);
    }

    @FuncLogger
    @FuncLambda(classFile = OutMessage.class, refs = { "logger" })
    public String out(@FuncParameter("message") String message) {
        return FuncString.format(message + "/no");
    }

    @Override
    @FuncLogger(name = "httpLink")
    @FuncCallback(DefaultCallback.class)
    @FuncLambda(classFile = HttpLink.class, refs = { "logger" })
    public Map<String, String> link() {
        Map<String, String> map = new HashMap<>(10);
        map.put("GitHub", "https://github.com/yiurhub");
        map.put("Gitee", "https://gitee.com/yiur");
        map.put("博客", "https://www.cnblogs.com/yiur-bgy");
        return map;
    }

}
