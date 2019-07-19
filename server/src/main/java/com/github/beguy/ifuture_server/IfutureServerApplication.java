package com.github.beguy.ifuture_server;

import com.github.beguy.ifuture_server.config.CacheConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableCaching
@Import(CacheConfiguration.class)
public class IfutureServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(IfutureServerApplication.class, args);
    }
}
