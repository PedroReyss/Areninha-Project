package com.cefsa.areninha.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Garante que a rota raiz leve para o login
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/login").setViewName("login");
    }
}