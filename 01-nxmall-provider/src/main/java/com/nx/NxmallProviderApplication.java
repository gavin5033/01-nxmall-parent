package com.nx;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author by 张益豪
 * @Classname NxmallProviderApplication
 * @Description TODO
 * @Date 2021/7/11 10:07
 */
@SpringBootApplication
@EnableDubbo
@MapperScan(basePackages = "com.nx.mapper")
public class NxmallProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(NxmallProviderApplication.class,args);
    }
}
