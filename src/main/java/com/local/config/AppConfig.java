package com.local.config;

import com.local.dao.factory.DAOFactory;
import com.local.dao.factory.DAOType;
import com.local.dao.factory.DaoTypeFactory;
import com.local.web.dto.ErrorResponseMapper;
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

//FIXME: substitute Set with List and Map with a List of Holder Objects to adhere to conventions

//FIXME: use Spring's validation instead of your own

//TODO: go back to constructor DI?

//TODO: switch to Boot and a single application.yml

//TODO: apply Custom AutoClose inside all DBDAOs? -> remove closeConnection() of TransactionManager
//TODO: one catch block inside DBDAO methods? no waiting for connection exception?
//TODO: better DAOExceptions being thrown up? as they don't go to the user they can be logged better
//TODO: DAO to work with ids too(like service)? unified layer interaction, less get methods, faster

//TODO: send model DTO over the web not the full object

//TODO: lazy fetch on aggregates like Cart and Payment(builder inside commonService)? or JPA?

//TODO: better structure of DB select queries? a fluent, table/DAO specific query constructor? or JOOQ?