package org.func.spring.boot.filter;

import org.func.spring.boot.annotation.ParseLog;
import org.func.spring.boot.component.watch.WatchInvoke;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Yiur
 */
public class ParseLogFilter implements ParseFilter {

        private WatchInvoke funcWatch;
        
        private String beanName;

        private String[] refs;

        private String replace;
        
        private Map<String, Object> parameter;
        
        private Object result;

        public ParseLogFilter() {
        }

        public ParseLogFilter(WatchInvoke funcWatch, String beanName, String[] refs, String replace, Map<String, Object> parameter, Object result) {
            this.funcWatch = funcWatch;
            this.beanName = beanName;
            this.refs = refs;
            this.replace = replace;
            this.parameter = parameter;
            this.result = result;
        }

        @Override
        public Object parse(Method[] methods) throws Throwable {
            for (Method method : methods) {
                method.setAccessible(true);
                ParseLog parseLog = method.getAnnotation(ParseLog.class);
                if (parseLog != null) {
                    String value = parseLog.value();
                    if (beanName.equals(value)) {
                        return method.invoke(funcWatch.getFuncWatch(), replace, parameter, result);
                    } else {
                        for (String ref : refs) {
                            if (ref.equals(value)) {
                                return method.invoke(funcWatch.getFuncWatch(), replace, parameter, result);
                            }
                        }
                    }
                }
            }
            return replace;
        }
        
    }