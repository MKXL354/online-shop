package com.local;

import com.local.dao.DAOType;
import com.local.dao.cart.CartDAO;
import com.local.dao.cart.CartDAOFactory;
import com.local.dao.product.ProductDAO;
import com.local.dao.product.ProductDAOFactory;
import com.local.dao.user.UserDAOFactory;
import com.local.service.productmanagement.ProductManagementService;
import com.local.service.user.UserService;
import com.local.servlet.mapper.ProductDTOMapper;
import com.local.util.objectvalidator.ObjectValidator;
import com.local.util.password.PasswordEncryptor;
import com.local.util.password.PasswordEncryptorImpl;
import com.local.dao.user.UserDAO;
import com.local.db.ConnectionPool;
import com.local.db.H2ConnectionPool;
import com.local.service.usermanagement.UserManagementService;
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

        UserDAO userDAODBImpl = UserDAOFactory.getUserDAO(DAOType.MEM, connectionPool);
        PasswordEncryptor passwordEncryptorImpl = new PasswordEncryptorImpl(1000, 32, 256);
        UserManagementService userManagementService = new UserManagementService(userDAODBImpl, passwordEncryptorImpl);
        sce.getServletContext().setAttribute("userManagementService", userManagementService);

        ProductDAO productDAOMemImpl = ProductDAOFactory.getProductDAO(DAOType.MEM, connectionPool);
        ProductManagementService productManagementService = new ProductManagementService(productDAOMemImpl);
        sce.getServletContext().setAttribute("productManagementService", productManagementService);

        String relativeTokenManagerConfigFileLocation = sce.getServletContext().getInitParameter("relativeTokenManagerConfigFileLocation");
        String absoluteTokenManagerConfigFileLocation = sce.getServletContext().getRealPath(relativeTokenManagerConfigFileLocation);
        TokenManager jwtManager = new JwtManager(absoluteTokenManagerConfigFileLocation, 15*60*1000);
        sce.getServletContext().setAttribute("tokenManager", jwtManager);

        ObjectValidator objectValidator = new ObjectValidator();
        sce.getServletContext().setAttribute("objectValidator", objectValidator);

        CommonWebComponentService commonWebComponentService = new CommonWebComponentService();
        sce.getServletContext().setAttribute("commonWebComponentService", commonWebComponentService);

        String relativeErrorResponseConfigLocation = sce.getServletContext().getInitParameter("relativeErrorResponseConfigFileLocation");
        String absoluteErrorResponseConfigLocation = sce.getServletContext().getRealPath(relativeErrorResponseConfigLocation);
        PropertyManager errorResponsePropertyManager = new PropertyManager(absoluteErrorResponseConfigLocation);
        sce.getServletContext().setAttribute("errorResponsePropertyManager", errorResponsePropertyManager);

        ProductDTOMapper productDTOMapper= new ProductDTOMapper(productManagementService);
        sce.getServletContext().setAttribute("productDTOMapper", productDTOMapper);

        CartDAO cartDAODBImpl = CartDAOFactory.getCartDAO(DAOType.MEM, connectionPool);
        UserService userService = new UserService(cartDAODBImpl, productDAOMemImpl);
        sce.getServletContext().setAttribute("userService", userService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        connectionPool.closePool();
        batchLogManager.shutDown();
    }
}
//TODO: services as interface to get supplied from outside
//TODO: userService depending on a paymentService
//TODO: solution for service concurrency: identity lock map inside service
//TODO: wallet payment inside payment service
//TODO: persistent payment entity

//TODO: initialize dao inside memory classes? separate class for saving state between runs

//TODO: IoC container, config file and relative path
//TODO: rewrite DB later