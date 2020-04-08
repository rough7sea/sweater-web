package com.roughsea.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.roughsea")
@EnableJpaRepositories(basePackages = "com.roughsea.repositories")
@EntityScan(basePackages = "com.roughsea.models")
@PropertySource(value = "classpath:application.properties", encoding="UTF-8")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}