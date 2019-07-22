package com.github.beguy.ifuture_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableCaching
@EnableRetry
public class IfutureServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(IfutureServerApplication.class, args);
    }
}
