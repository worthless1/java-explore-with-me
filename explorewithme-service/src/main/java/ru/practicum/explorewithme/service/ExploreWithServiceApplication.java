package ru.practicum.explorewithme.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"ru.practicum"})
public class ExploreWithServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExploreWithServiceApplication.class, args);
    }
}