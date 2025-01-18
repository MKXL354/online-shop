package com.local.config;

import com.local.dto.ErrorResponseMapper;
import com.local.exception.common.ApplicationRuntimeException;
import com.local.util.property.PropertyManager;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
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
            return PropertyManager.loadProperties(env.getProperty("hibernate.propertiesLocation"));
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
}

//TODO: more testing? especially concurrency
//TODO: refactor the name of controllers and services
//TODO: go back to constructor DI?
//TODO: switch to Boot and a single application.yml