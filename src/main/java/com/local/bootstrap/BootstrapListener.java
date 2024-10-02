package com.local.bootstrap;

import com.local.dao.UserDAO;
import com.local.db.ConnectionPool;
import com.local.db.DatabaseConfig;
import com.local.db.H2ConnectionPool;
import com.local.service.UserManagementService;
import com.local.servlet.CommonServletServices;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class BootstrapListener implements ServletContextListener {
    private final String relativeDatabaseConfigFileLocation = "/WEB-INF/classes/db.properties";
    private ConnectionPool connectionPool;

    @Override
    public void contextInitialized(ServletContextEvent sce){
        String absoluteDatabaseConfigFileLocation = sce.getServletContext().getRealPath(relativeDatabaseConfigFileLocation);
        openDatabaseConnectionPool(absoluteDatabaseConfigFileLocation);
        UserDAO userDAO = new UserDAO(connectionPool);
        UserManagementService userManagementService = new UserManagementService(userDAO);
        sce.getServletContext().setAttribute("userManagementService", userManagementService);
        CommonServletServices commonServletServices = new CommonServletServices();
        sce.getServletContext().setAttribute("commonServletServices", commonServletServices);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        connectionPool.closePool();
    }

    private void openDatabaseConnectionPool(String absoluteDatabaseConfigFileLocation){
        try{
            DatabaseConfig databaseConfig = new DatabaseConfig(absoluteDatabaseConfigFileLocation);
            this.connectionPool = new H2ConnectionPool(databaseConfig);
            connectionPool.openPool();
        }
        catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }
}