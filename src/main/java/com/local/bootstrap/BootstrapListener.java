package com.local.bootstrap;

import com.local.dao.UserDAO;
import com.local.db.ConnectionPool;
import com.local.db.DatabaseConfig;
import com.local.db.H2ConnectionPool;
import com.local.service.UserManagementService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class BootstrapListener implements ServletContextListener {
    private final String databaseConfigFileLocation = "resources/db.properties";
    private ConnectionPool connectionPool;

    @Override
    public void contextInitialized(ServletContextEvent sce){
        setConnectionPool();
        UserDAO userDAO = new UserDAO(connectionPool);
        UserManagementService userManagementService = new UserManagementService(userDAO);
        sce.getServletContext().setAttribute("userManagementService", userManagementService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        connectionPool.closePool();
    }

    private void setConnectionPool(){
        try{
            DatabaseConfig databaseConfig = new DatabaseConfig(databaseConfigFileLocation);
            this.connectionPool = new H2ConnectionPool(databaseConfig);
        }
        catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }
}
