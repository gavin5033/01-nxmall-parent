package com.nx;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author by 张益豪
 * @Classname NxmallManagerApplication
 * @Description TODO
 * @Date 2021/7/11 10:11
 */
@SpringBootApplication
@EnableDubbo
public class NxmallManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(NxmallManagerApplication.class,args);
    }
}
