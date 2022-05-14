package com.leonduri.d7back;

import io.swagger.models.HttpMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class D7backApplication {

    @Value("${spring.servlet.multipart.docker-location}")
    private String multifilePath;

    public static void main(String[] args) {
        SpringApplication.run(D7backApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping("/**")
                        .allowedOrigins("*")
                        .exposedHeaders("X-AUTH-TOKEN", "REFRESH-TOKEN")
                        .allowedMethods(
                                HttpMethod.GET.name(),
                                HttpMethod.HEAD.name(),
                                HttpMethod.POST.name(),
                                HttpMethod.PUT.name(),
                                HttpMethod.DELETE.name()
                        );
            }
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                ResourceHandlerRegistration resourceHandlerRegistration = registry.addResourceHandler("/videos/**")
                        .addResourceLocations(multifilePath)
                        .setCachePeriod(20);
            }
        };
    }
}
