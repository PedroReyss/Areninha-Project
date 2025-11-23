package com.cefsa.areninha.config;


import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
public class RouteDiagnostic {

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext
            .getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        
        System.out.println("🎯 ROTAS MAPEADAS:");
        requestMappingHandlerMapping.getHandlerMethods().forEach((key, value) -> {
            System.out.println("📍 " + key + " -> " + value);
        });
    }
}