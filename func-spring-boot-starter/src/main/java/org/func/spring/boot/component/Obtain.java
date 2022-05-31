package org.func.spring.boot.component;

import org.func.spring.boot.link.FuncLink;

/**
 * @author Yiur
 */
public class Obtain {

    public static <T> T get(FuncLink funcLink, String beanName, String[] refs, String link) {
        T type = funcLink.getObject(beanName, link);
        if (type == null) {
            for (String ref : refs) {
                type = funcLink.getObject(ref, link);
                if (type != null) {
                    break;
                }
            }
        }
        return type;
    }

}
