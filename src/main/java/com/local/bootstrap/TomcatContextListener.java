package com.local.bootstrap;

import com.local.dao.UserDAO;
import com.local.service.UserManagementService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.h2.jdbcx.JdbcConnectionPool;

public class TomcatContextListener implements ServletContextListener {
//    TODO: use DataSource abstraction? DAO is currently tied to h2 pool. Maybe a simple wrapper suffices
    private JdbcConnectionPool connectionPool;

    @Override
    public void contextInitialized(ServletContextEvent sce){
        setConnectionPool();
        UserDAO userDAO = new UserDAO(connectionPool);
        UserManagementService userManagementService = new UserManagementService(userDAO);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        connectionPool.dispose();
    }

    private void setConnectionPool(){
//        TODO: config .properties file: exception instead of compilation error for outside users. also JNDI?
        String url = DatabaseDetail.URL;
        String username = DatabaseDetail.USERNAME;
        String password = DatabaseDetail.PASSWORD;
        this.connectionPool = JdbcConnectionPool.create(url, username, password);
    }
}
