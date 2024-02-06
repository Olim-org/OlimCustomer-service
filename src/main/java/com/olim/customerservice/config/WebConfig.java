package com.olim.customerservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {


    public static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH";

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods(ALLOWED_METHOD_NAMES.split(","))
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedOrigins("http://localhost:3000", "http://localhost:8080", "https://olim-crm-front.vercel.app", "http://localhost", "https://localhost");
    }

//    @Bean
//    public WebSecurityCustomizer ignoringCustomizer() {
//        return (web) -> web.ignoring().requestMatchers("/h2-console/**");
//    }



}