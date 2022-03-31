package com.simple.web.func;

import org.springframework.stereotype.Component;

/**
 * @author Yiur
 */
@Component
public interface HttpLink {

    /**
     * 返回关联
     * @return object
     */
    Object link();

}
