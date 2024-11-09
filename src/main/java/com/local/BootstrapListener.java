package com.local;

import com.local.dao.DAOType;
import com.local.dao.cart.CartDAO;
import com.local.dao.cart.CartDAOFactory;
import com.local.dao.payment.PaymentDAO;
import com.local.dao.payment.PaymentDAOFactory;
import com.local.dao.product.ProductDAO;
import com.local.dao.product.ProductDAOFactory;
import com.local.dao.user.UserDAOFactory;
import com.local.service.payment.PaymentService;
import com.local.service.productmanagement.ProductManagementService;
import com.local.service.user.UserService;
import com.local.servlet.mapper.ProductDTOMapper;
import com.local.util.lock.LockManager;
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

        UserDAO userDAOImpl = UserDAOFactory.getUserDAO(DAOType.MEM, null);
        ProductDAO productDAOImpl = ProductDAOFactory.getProductDAO(DAOType.MEM, null);
        CartDAO cartDAODImpl = CartDAOFactory.getCartDAO(DAOType.MEM, null);
        PaymentDAO paymentDAOImpl = PaymentDAOFactory.getPaymentDAO(DAOType.MEM, null);

        LockManager lockManager = new LockManager();

        PasswordEncryptor passwordEncryptorImpl = new PasswordEncryptorImpl(1000, 32, 256);
        UserManagementService userManagementService = new UserManagementService(userDAOImpl, passwordEncryptorImpl, lockManager);
        sce.getServletContext().setAttribute("userManagementService", userManagementService);

        ProductManagementService productManagementService = new ProductManagementService(productDAOImpl, lockManager);
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

        UserService userService = new UserService(cartDAODImpl, productDAOImpl, userDAOImpl, paymentDAOImpl, lockManager);
        sce.getServletContext().setAttribute("userService", userService);

        PaymentService paymentService = new PaymentService(userDAOImpl, paymentDAOImpl, lockManager);
        sce.getServletContext().setAttribute("paymentService", paymentService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Shutting down ...");
        connectionPool.closePool();
        batchLogManager.shutDown();
    }
}
//TODO: services as interface to get supplied from outside?

//TODO: reconfigure the filters in web.xml
//TODO: a better filter design: have admin/ and web-user/ endpoints to control their accessible actions

//TODO: IoC container, config file and relative path

//TODO: rewrite DB later

//TODO: maybe use response objects for methods to avoid excessive exceptions and dispersed response logic