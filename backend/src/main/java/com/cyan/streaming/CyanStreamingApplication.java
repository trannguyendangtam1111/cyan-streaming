package com.cyan.streaming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.cyan")
@EnableScheduling
public class CyanStreamingApplication {

    public static void main(String[] args) {
        SpringApplication.run(CyanStreamingApplication.class, args);
    }
}
