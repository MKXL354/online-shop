package com.local;

import com.local.dao.product.ProductDAO;
import com.local.dao.product.ProductDAOMemImpl;
import com.local.service.ProductManagementService;
import com.local.util.password.PasswordEncryptor;
import com.local.util.password.PasswordEncryptorImpl;
import com.local.dao.user.UserDAO;
import com.local.dao.user.UserDAODBImpl;
import com.local.db.ConnectionPool;
import com.local.db.H2ConnectionPool;
import com.local.service.UserManagementService;
import com.local.servlet.CommonWebComponentService;
import com.local.util.token.JwtManager;
import com.local.util.token.TokenManager;
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

        UserDAO userDAODBImpl = new UserDAODBImpl(connectionPool);
        PasswordEncryptor passwordEncryptorImpl = new PasswordEncryptorImpl(1000, 32, 256);
        UserManagementService userManagementService = new UserManagementService(userDAODBImpl, passwordEncryptorImpl);
        sce.getServletContext().setAttribute("userManagementService", userManagementService);

        ProductDAO productDAOMemImpl = new ProductDAOMemImpl();
        ProductManagementService productManagementService = new ProductManagementService(productDAOMemImpl);
        sce.getServletContext().setAttribute("productManagementService", productManagementService);

        String relativeTokenManagerConfigFileLocation = sce.getServletContext().getInitParameter("relativeTokenManagerConfigFileLocation");
        String absoluteTokenManagerConfigFileLocation = sce.getServletContext().getRealPath(relativeTokenManagerConfigFileLocation);
        TokenManager jwtManager = new JwtManager(absoluteTokenManagerConfigFileLocation, /*15*60*/5000);
        sce.getServletContext().setAttribute("tokenManager", jwtManager);

        CommonWebComponentService commonWebComponentService = new CommonWebComponentService(jwtManager);
        sce.getServletContext().setAttribute("commonWebComponentService", commonWebComponentService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        connectionPool.closePool();
        batchLogManager.shutDown();
    }
}
//TODO: config using a file not injection