package com.local.bootstrap;

import com.local.dao.PasswordEncryptor;
import com.local.dao.PasswordEncryptorImpl;
import com.local.dao.UserDAO;
import com.local.dao.UserDAOImpl;
import com.local.db.ConnectionPool;
import com.local.db.H2ConnectionPool;
import com.local.service.UserManagementService;
import com.local.servlet.CommonServletService;
import com.local.service.JwtManager;
import com.local.service.TokenManager;
import com.local.util.PropertyManager;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class BootstrapListener implements ServletContextListener {
    private String relativeDatabaseConfigFileLocation = "/WEB-INF/classes/db.properties";
    private String relativeTokenManagerConfigFileLocation = "/WEB-INF/classes/tokenManager.properties";
    private ConnectionPool connectionPool;

    @Override
    public void contextInitialized(ServletContextEvent sce){
        String absoluteDatabaseConfigFileLocation = sce.getServletContext().getRealPath(relativeDatabaseConfigFileLocation);
        PropertyManager databasePropertyManager = new PropertyManager(absoluteDatabaseConfigFileLocation);
        this.connectionPool = new H2ConnectionPool(databasePropertyManager, 5, 500);
        connectionPool.openPool();

        UserDAO userDAOImpl = new UserDAOImpl(connectionPool);
        PasswordEncryptor passwordEncryptorImpl = new PasswordEncryptorImpl(1000, 32, 256);
        UserManagementService userManagementService = new UserManagementService(userDAOImpl, passwordEncryptorImpl);
        sce.getServletContext().setAttribute("userManagementService", userManagementService);

        CommonServletService commonServletService = new CommonServletService();
        sce.getServletContext().setAttribute("commonServletServices", commonServletService);

        String absoluteTokenManagerConfigFileLocation = sce.getServletContext().getRealPath(relativeTokenManagerConfigFileLocation);
        TokenManager jwtManager = new JwtManager(absoluteTokenManagerConfigFileLocation, 10000);
        sce.getServletContext().setAttribute("tokenManager", jwtManager);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        connectionPool.closePool();
    }
}