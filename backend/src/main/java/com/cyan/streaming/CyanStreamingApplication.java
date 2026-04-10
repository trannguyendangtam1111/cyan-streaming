package com.cyan.streaming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.cyan")
public class CyanStreamingApplication {

    public static void main(String[] args) {
        SpringApplication.run(CyanStreamingApplication.class, args);
    }
}
