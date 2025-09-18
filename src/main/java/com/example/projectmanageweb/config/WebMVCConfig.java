package com.example.projectmanageweb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("decorators/index");
        registry.addViewController("/login").setViewName("commons/login");
        registry.addViewController("/register").setViewName("commons/register");
        registry.addViewController("/user/home").setViewName("users/home");
        registry.addViewController("/profile").setViewName("commons/profile");
        registry.addViewController("/reset/request").setViewName("commons/forgotpassword");
        registry.addViewController("/reset/change").setViewName("commons/changepassword");
        registry.addViewController("/projects/create").setViewName("users/projectdashboard");

    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/");
    }

}
