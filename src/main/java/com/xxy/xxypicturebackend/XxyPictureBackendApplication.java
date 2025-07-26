package com.xxy.xxypicturebackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@MapperScan("com.xxy.xxypicturebackend.mapper")
@EnableAspectJAutoProxy(exposeProxy = true)
public class XxyPictureBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(XxyPictureBackendApplication.class, args);
    }

}
