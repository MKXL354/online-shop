package com.local.config;

import com.local.dto.ErrorResponseMapper;
import com.local.exception.common.ApplicationRuntimeException;
import com.local.util.property.PropertyManager;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "com.local")
@PropertySource("classpath:application.properties")
@EnableJpaRepositories(basePackages = "com.local.repository")
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AppConfig {
    private Environment env;

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }

    @Bean
    public Properties errorResponseProperties(@Value("${err.propertiesLocation}") String propertiesLocation) {
        try {
            return PropertyManager.loadProperties(propertiesLocation);
        } catch (IOException e) {
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
    }

    @Bean
    public ErrorResponseMapper errorResponseMapper(@Qualifier("errorResponseProperties") Properties properties) {
        ErrorResponseMapper errorResponseMapper = new ErrorResponseMapper();
        properties.stringPropertyNames().forEach(
                (type) -> errorResponseMapper.addErrorMapping(type, Integer.parseInt(properties.getProperty(type))));
        return errorResponseMapper;
    }

    @Bean
    public DataSource dataSource() {
        Properties props;
        try {
            props = PropertyManager.loadProperties(env.getProperty("ds.propertiesLocation"));
        } catch (IOException e) {
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
        String driverClassName = props.getProperty("driverClassName");
        String url = props.getProperty("url");
        String username = props.getProperty("username");
        String password = props.getProperty("password");
        int maximumPoolSize = Integer.parseInt(props.getProperty("hikari.maximum-pool-size"));
        int connectionTimeout = Integer.parseInt(props.getProperty("hikari.connection-timeout"));
        int idleTimeout = Integer.parseInt(props.getProperty("hikari.idle-timeout"));

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMaximumPoolSize(maximumPoolSize);
        dataSource.setConnectionTimeout(connectionTimeout);
        dataSource.setIdleTimeout(idleTimeout);
        return dataSource;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    public Properties jpaProperties() {
        try {
            return PropertyManager.loadProperties(env.getProperty("jpa.propertiesLocation"));
        } catch (IOException e) {
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean  entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource());
        factory.setPackagesToScan("com.local.entity");
        factory.setJpaVendorAdapter(jpaVendorAdapter());
        factory.setJpaProperties(jpaProperties());
        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public Validator validator(){
        return Validation.buildDefaultValidatorFactory().getValidator();
    }
}

//TODO: refactor the name of controllers

//TODO: input of controller and service: DTO. output of controller: DTO. output of Service: Entity

//TODO: dedicated controller for bank account

//TODO: DAOException removed? maybe not bring it back as it is business related

//FIXME: Don't forget to use @Valid in your controllers

//TODO: payment cache?

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

//TODO: send entity DTO over the web not the full object

//TODO: lazy fetch on aggregates like Cart and Payment(builder inside commonService)? or JPA?

//TODO: better structure of DB select queries? a fluent, table/DAO specific query constructor? or JOOQ?