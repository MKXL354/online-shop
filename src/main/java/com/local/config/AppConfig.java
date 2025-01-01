package com.local.config;

import com.local.dao.factory.DAOFactory;
import com.local.dao.factory.DAOType;
import com.local.dao.factory.DaoTypeFactory;
import com.local.dto.ErrorResponseMapper;
import com.local.exception.common.ApplicationRuntimeException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "com.local")
@PropertySource("classpath:application.properties")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AppConfig {
    @Bean
    public DAOFactory daoFactory(){
        return DaoTypeFactory.getFactory(DAOType.DB);
    }

    @Bean
    public Properties errorResponseProperties(@Value("${err.PropertiesLocation}") String propertiesLocation){
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(propertiesLocation)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
        return properties;
    }

    @Bean
    public ErrorResponseMapper errorResponseMapper(@Qualifier("errorResponseProperties") Properties properties){
        ErrorResponseMapper errorResponseMapper = new ErrorResponseMapper();
        properties.stringPropertyNames().forEach(
                (type) -> errorResponseMapper.addErrorMapping(type, Integer.parseInt(properties.getProperty(type))));
        return errorResponseMapper;
    }
}
