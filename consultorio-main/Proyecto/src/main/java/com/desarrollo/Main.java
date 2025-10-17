package com.desarrollo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author ivanLuna
 */
@SpringBootApplication( scanBasePackages = { "com.desarrollo.*"} )
public class Main {

    public static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        context = SpringApplication.run(Main.class, args);
        context.start();
    }
}
