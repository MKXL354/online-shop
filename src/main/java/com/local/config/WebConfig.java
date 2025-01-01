package com.local.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.local.config.manual.GsonHttpMessageConverter;
import com.local.config.manual.GsonLocalDateTimeAdapter;
import com.local.config.manual.GsonProductDeserializer;
import com.local.model.Product;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.local.controller")
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.addFirst(new GsonHttpMessageConverter(gson()));
    }

    @Bean
    public Gson gson(){
        return new GsonBuilder().registerTypeAdapter(Product.class, new GsonProductDeserializer())
                .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
                .create();
    }
}
