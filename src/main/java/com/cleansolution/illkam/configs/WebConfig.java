package com.cleansolution.illkam.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(
                                "http://localhost:3000",
                                "http://admin.ilkkam.com.s3-website.ap-northeast-2.amazonaws.com",
                                "http://www.ilkkam.com.s3-website.ap-northeast-2.amazonaws.com",
                                "http://localhost:51285",
                                "http://localhost:62823",
                                "https://www.ilkkam.com",
                                "http://www.ilkkam.com",
                                "http://com.illkam.co.kr.s3-website.ap-northeast-2.amazonaws.com") // Flutter Web의 Origin

                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
