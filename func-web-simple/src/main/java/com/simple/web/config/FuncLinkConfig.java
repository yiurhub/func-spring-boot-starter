package com.simple.web.config;

import com.simple.web.plugin.SimpleCallbackPlugin;
import com.simple.web.plugin.SimpleLifePlugin;
import com.simple.web.plugin.SimpleLoggerPlugin;
import org.func.spring.boot.annotation.EnableFuncLambda;
import org.func.spring.boot.component.callback.FuncCallback;
import org.func.spring.boot.component.callback.FuncCallbackError;
import org.func.spring.boot.component.callback.FuncCallbackThen;
import org.func.spring.boot.component.life.FuncLife;
import org.func.spring.boot.component.life.FuncLifeEnd;
import org.func.spring.boot.component.life.FuncLifeStart;
import org.func.spring.boot.component.logger.FuncLogger;
import org.func.spring.boot.factory.FuncLinkFactory;
import org.func.spring.boot.factory.agent.FuncLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;


/**
 * @author Yiur
 */
@Configuration
@EnableFuncLambda("com.simple.web.lambda")
public class FuncLinkConfig {

    @Autowired
    public FuncLinkFactory funcLinkFactory;

    @Bean("simple")
    public FuncLink funcLink() {
        return funcLinkFactory.build()
                // logger
                .<FuncLogger>setObject("logger:log", (set, log) -> "logger => ${methodName}\r\n")
                // httpLink
                .<FuncLife<Object, Object>>setObject("httpLink:life", new FuncLife<Object, Object>() {
                    @Override
                    public Map<String, Object> start(Map<String, Object> args) {
                        return args;
                    }

                    @Override
                    public Object end(Object result) {
                        return result;
                    }
                })
                .<FuncLifeStart>setObject("httpLink:life-start", args -> args)
                .<FuncLifeEnd<Object, Object>>setObject("httpLink:life-end", result -> result)
                .<FuncCallback<Map<String, String>, Object>>setObject("httpLink:callback", new FuncCallback<Map<String, String>, Object>() {
                    @Override
                    public Object then(Map<String, String> result) {
                        return result;
                    }

                    @Override
                    public Object error(Throwable throwable) {
                        return throwable;
                    }
                })
                .<FuncCallbackThen<Map<String, String>, Object>>setObject("httpLink:callback-then", result -> result)
                .<FuncCallbackError<Object>>setObject("httpLink:callback-error", throwable -> throwable)
                .setObject("httpLink:FuncLifePlugin", SimpleLifePlugin.class)
                .setObject("httpLink:FuncCallbackPlugin", SimpleCallbackPlugin.class)
                .setObject("httpLink:FuncLoggerPlugin", SimpleLoggerPlugin.class)
                // outMessage
                .<FuncLifeStart>setObject("outMessage:life-start", data -> data)
                .<FuncLifeEnd<String, String>>setObject("outMessage:life-end", data -> data)
                .<FuncCallbackThen<String, String>>setObject("outMessage:callback-then", data -> data)
                .<FuncCallbackError<String>>setObject("outMessage:callback-error", Throwable::getMessage);
    }

}
