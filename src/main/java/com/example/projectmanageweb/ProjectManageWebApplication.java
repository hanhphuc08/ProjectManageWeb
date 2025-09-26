package com.example.projectmanageweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProjectManageWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectManageWebApplication.class, args);
    }

}
