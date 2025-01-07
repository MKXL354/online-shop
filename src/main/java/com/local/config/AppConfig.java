package com.local.config;

import com.local.persistence.factory.DAOFactory;
import com.local.persistence.factory.DAOType;
import com.local.persistence.factory.DaoTypeFactory;
import com.local.util.property.PropertyManager;
import com.local.dto.ErrorResponseMapper;
import com.local.exception.common.ApplicationRuntimeException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

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
        try {
            return PropertyManager.loadProperties(propertiesLocation);
        } catch (IOException e) {
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
    }

    @Bean
    public ErrorResponseMapper errorResponseMapper(@Qualifier("errorResponseProperties") Properties properties){
        ErrorResponseMapper errorResponseMapper = new ErrorResponseMapper();
        properties.stringPropertyNames().forEach(
                (type) -> errorResponseMapper.addErrorMapping(type, Integer.parseInt(properties.getProperty(type))));
        return errorResponseMapper;
    }
}

//TODO: clean up at the end (remove the excluded and extra files)

//TODO: use Hibernate/JPA and Spring's Transaction Manager

//FIXME: Fix the DAO abstract factory to not create all the objects

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