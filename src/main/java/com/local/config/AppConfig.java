package com.local.config;

import com.local.dao.factory.DAOFactory;
import com.local.dao.factory.DAOType;
import com.local.dao.factory.DaoTypeFactory;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = "com.local")
@PropertySource("classpath:application.properties")
@EnableAspectJAutoProxy(proxyTargetClass = true)
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
