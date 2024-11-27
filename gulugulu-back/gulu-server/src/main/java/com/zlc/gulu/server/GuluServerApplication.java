package com.zlc.gulu.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class GuluServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuluServerApplication.class, args);
    }

}
