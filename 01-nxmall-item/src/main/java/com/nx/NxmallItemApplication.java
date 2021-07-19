package com.nx;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author by 张益豪
 * @Classname NxmallItemApplication
 * @Description TODO
 * @Date 2021/7/18 1:11
 */
@SpringBootApplication
@EnableDubbo
public class NxmallItemApplication {

    public static void main(String[] args) {
        SpringApplication.run(NxmallItemApplication.class, args);
    }

}
