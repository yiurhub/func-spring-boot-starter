package com.simple.web.controller;

import com.simple.web.func.HttpLink;
import com.simple.web.func.OutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yiur
 */
@RestController
public class ResultController {

    @Autowired
    public OutMessage outMessage;

    @Autowired
    public HttpLink httpLink;

    @RequestMapping("/replace")
    public String replace(String message, String... args) {
        return outMessage.out(message, args);
    }

    @RequestMapping("/replace/no")
    public String replace(String message) {
        return outMessage.out(message);
    }

    @RequestMapping("/hello/{message}")
    public String hello(@PathVariable("message") String message) {
        return outMessage.out(message);
    }

    @RequestMapping("/httpLink")
    public Object httpLink() {
        return httpLink.link();
    }

}
