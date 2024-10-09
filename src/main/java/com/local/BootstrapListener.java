package com.local;

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
import com.local.util.logging.BatchLogManager;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class BootstrapListener implements ServletContextListener {
    private ConnectionPool connectionPool;
    private BatchLogManager batchLogManager;

    @Override
    public void contextInitialized(ServletContextEvent sce){
        String relativeDatabaseConfigFileLocation = sce.getServletContext().getInitParameter("relativeDatabaseConfigFileLocation");
        String absoluteDatabaseConfigFileLocation = sce.getServletContext().getRealPath(relativeDatabaseConfigFileLocation);
        PropertyManager databasePropertyManager = new PropertyManager(absoluteDatabaseConfigFileLocation);
        connectionPool = new H2ConnectionPool(databasePropertyManager, 5, 500);
        connectionPool.openPool();

        String relativeBatchLogOutputDirectory = sce.getServletContext().getInitParameter("relativeBatchLogOutputDirectory");
        String absoluteBatchLogOutputDirectory = sce.getServletContext().getRealPath(relativeBatchLogOutputDirectory);
        batchLogManager = new BatchLogManager(absoluteBatchLogOutputDirectory, 5000, 10);
        batchLogManager.start();
        sce.getServletContext().setAttribute("batchLogManager", batchLogManager);

        UserDAO userDAOImpl = new UserDAOImpl(connectionPool);
        PasswordEncryptor passwordEncryptorImpl = new PasswordEncryptorImpl(1000, 32, 256);
        UserManagementService userManagementService = new UserManagementService(userDAOImpl, passwordEncryptorImpl);
        sce.getServletContext().setAttribute("userManagementService", userManagementService);

        CommonServletService commonServletService = new CommonServletService();
        sce.getServletContext().setAttribute("commonServletServices", commonServletService);

        String relativeTokenManagerConfigFileLocation = sce.getServletContext().getInitParameter("relativeTokenManagerConfigFileLocation");
        String absoluteTokenManagerConfigFileLocation = sce.getServletContext().getRealPath(relativeTokenManagerConfigFileLocation);
        TokenManager jwtManager = new JwtManager(absoluteTokenManagerConfigFileLocation, 15*60*1000);
        sce.getServletContext().setAttribute("tokenManager", jwtManager);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        connectionPool.closePool();
        batchLogManager.shutDown();
    }
}