package com.comp2005;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.comp2005") // Replace with your package name
public class Comp2005Application {
    public static void main(String[] args) {
        SpringApplication.run(Comp2005Application.class, args);
    }
}
