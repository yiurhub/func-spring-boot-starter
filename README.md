# func-spring-boot-starter

#### func-spring-boot-starter 介绍

项目地址: https://gitee.com/yiur/func-web-simple

> 核心注解

```java
@EnableFuncLambda	//开启匿名函数代理
@FuncConfiguration	//匿名函数配置
@FuncLambda			//匿名函数
@EnableLog			//开启匿名函数调用前函数信息
@CallbackClass		//绑定回调函数类
```

> @EnableFuncLambda

```java
@EnableWebMvc
@EnableFuncLambda("com.simple.web.lambda")
@SpringBootApplication
public class FuncApplication {

    public static void main(String[] args) {
        SpringApplication.run(FuncApplication.class, args);
    }

}
```

> @FuncConfiguration

```java
@FuncConfiguration
public class LambdaConfig {
	//code...
}
```

> @FuncLambda

```java
@FuncLambda(classFile = OutMessage.class, refs = { "blog-log" })
public String out(@FuncParameter("message") String message, @FuncParameter("args") String... args) {
	return FuncString.format(message, args);
}
```

> @EnableLog

```java
@EnableLog
@FuncLambda(classFile = OutMessage.class, refs = { "blog-log" })
public String out(@FuncParameter("message") String message) {
	return FuncString.format(message);
}
```

> @CallbackClass

```java
@EnableLog
@CallbackClass(DefaultCallback.class)
@FuncLambda(classFile = OutMessage.class, refs = { "blog-log" })
public Map<String, String> link() {
    Map<String, String> map = new HashMap<>(10);
    map.put("百度", "http://www.baidu.com");
    map.put("BiliBili", "http://www.bilibili.com");
    map.put("localhost", "http://localhost:7000");
    map.put("博客", "https://www.cnblogs.com/yiur-bgy/p/15521428.html");
    return map;
}
```

> FuncLink 配置匿名函数链接

```java
@Configuration
public class FuncLinkConfig {

    @Autowired
    public FuncFactory funcFactory;

    @Bean
    public FuncLink funcLink() {
        FuncLink build = funcFactory.build()
                .<FuncLogger>setObject("link-log:log", set -> "link-log => ?{methodName}");
        setBlogInitOutInfo(build);
        return build;
    }

    private void setBlogInitOutInfo(FuncLink funcLink) {
        funcLink // OutMessage State of life
                .<FuncLifeStart>setObject(funcToolKey.splice("outMessage", FuncToolType.LIFE_START_KEY), data -> {
                    if (data.size() > 1) {
                        data.put("message", FuncString.format("start() -> { ? }", data.get("message")));
                    }
                    return data;
                })

                .<FuncCallbackThen<String, String>>setObject("outMessage:callback-then", data -> FuncString.format("[ { \"key\": \"replace\", \"value\": \"?\" } ]", data))

                .<FuncCallbackError<String>>setObject("outMessage:callback-error", error -> error instanceof InvocationTargetException ? ((InvocationTargetException) error).getTargetException().getMessage() : error.getMessage())

                .<FuncLifeEnd<String, String>>setObject("outMessage:life-end", data -> data);
    }
    
}
```
