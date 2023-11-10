package com.theocean.fundering;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@EnableAsync
@SpringBootApplication
public class FunderingApplication {

    public static void main(final String[] args) {
        SpringApplication.run(FunderingApplication.class, args);
    }

}
