package com.simple.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author Yiur
 */
@EnableWebMvc
@SpringBootApplication
public class FuncApplication {

    public static void main(String[] args) {
        SpringApplication.run(FuncApplication.class, args);
    }

}
