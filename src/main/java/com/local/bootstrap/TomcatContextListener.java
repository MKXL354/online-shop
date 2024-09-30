package com.local.bootstrap;

import com.local.dao.UserDAO;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.h2.jdbcx.JdbcConnectionPool;

public class TomcatContextListener implements ServletContextListener {
    private JdbcConnectionPool connectionPool;

    @Override
    public void contextInitialized(ServletContextEvent sce){
        setConnectionPool();
        UserDAO userDAO = new UserDAO(connectionPool);
        sce.getServletContext().setAttribute("userDAO", userDAO);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        connectionPool.dispose();
    }

    private void setConnectionPool(){
        String url = DatabaseDetail.URL;
        String username = DatabaseDetail.USERNAME;
        String password = DatabaseDetail.PASSWORD;
        this.connectionPool = JdbcConnectionPool.create(url, username, password);
    }
}
