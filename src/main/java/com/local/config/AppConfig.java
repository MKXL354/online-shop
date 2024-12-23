package com.local.config;

import com.local.dao.factory.DAOFactory;
import com.local.dao.factory.DAOType;
import com.local.dao.factory.DaoTypeFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "com.local")
@PropertySource("classpath:application.properties")
public class AppConfig {
    @Bean
    public DAOFactory daoFactory(){
        return DaoTypeFactory.getFactory(DAOType.DB);
    }

//    @Bean
//    public LogManagerFactoryBean logManagerFactoryBean(){
//        return new LogManagerFactoryBean();
//    }
}
//TODO: AOP and automatic proxy
//TODO: write the controllers and change the web API
