package com.app.tradeguess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.app.tradeguess")
public class TradeGuessApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeGuessApplication.class, args);
    }

}
