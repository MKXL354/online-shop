package com.local;

import com.local.dao.DAOType;
import com.local.dao.cart.CartDAO;
import com.local.dao.cart.CartDAOFactory;
import com.local.dao.payment.PaymentDAO;
import com.local.dao.payment.PaymentDAOFactory;
import com.local.dao.product.ProductDAO;
import com.local.dao.product.ProductDAOFactory;
import com.local.dao.user.UserDAOFactory;
import com.local.model.Product;
import com.local.model.ProductStatus;
import com.local.service.UtilityService;
import com.local.service.payment.PaymentService;
import com.local.service.productmanagement.ProductManagementService;
import com.local.service.scheduler.PurchaseRollbackScheduler;
import com.local.service.scheduler.ReserveRollbackScheduler;
import com.local.service.scheduler.RollbackScheduler;
import com.local.service.user.UserService;
import com.local.util.lock.LockManager;
import com.local.util.objectvalidator.ObjectValidator;
import com.local.util.password.PasswordEncryptor;
import com.local.util.password.PasswordEncryptorImpl;
import com.local.dao.user.UserDAO;
import com.local.dbconnector.ConnectionPool;
import com.local.dbconnector.H2ConnectionPool;
import com.local.service.usermanagement.UserManagementService;
import com.local.servlet.CommonWebComponentService;
import com.local.util.persistence.SerializedPersistenceManager;
import com.local.util.token.JwtManager;
import com.local.util.token.TokenManager;
import com.local.util.PropertyManager;
import com.local.util.logging.BatchLogManager;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class BootstrapListener implements ServletContextListener {
    private ConnectionPool connectionPool;
    private SerializedPersistenceManager serializedPersistenceManager;
    private BatchLogManager batchLogManager;

    private RollbackScheduler purchaseRollbackScheduler;
    private RollbackScheduler reserveRollbackScheduler;

    private UserDAO userDAOImpl;
    private ProductDAO productDAOImpl;
    private CartDAO cartDAOImpl;
    private PaymentDAO paymentDAOImpl;

    @Override
    public void contextInitialized(ServletContextEvent sce){
        serializedPersistenceManager = new SerializedPersistenceManager();

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

        userDAOImpl = UserDAOFactory.getUserDAO(DAOType.MEM, connectionPool, serializedPersistenceManager);
        productDAOImpl = ProductDAOFactory.getProductDAO(DAOType.MEM, connectionPool, serializedPersistenceManager);
        cartDAOImpl = CartDAOFactory.getCartDAO(DAOType.MEM, connectionPool);
        paymentDAOImpl = PaymentDAOFactory.getPaymentDAO(DAOType.MEM, connectionPool);

        LockManager lockManager = new LockManager();

        UtilityService utilityService = new UtilityService(userDAOImpl, paymentDAOImpl);
        sce.getServletContext().setAttribute("utilityService", utilityService);

        PasswordEncryptor passwordEncryptorImpl = new PasswordEncryptorImpl(1000, 32, 256);
        UserManagementService userManagementService = new UserManagementService(userDAOImpl, passwordEncryptorImpl);
        sce.getServletContext().setAttribute("userManagementService", userManagementService);

        ProductManagementService productManagementService = new ProductManagementService(productDAOImpl);
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

        UserService userService = new UserService(utilityService, cartDAOImpl, productDAOImpl, userDAOImpl, paymentDAOImpl);
        sce.getServletContext().setAttribute("userService", userService);

        PaymentService paymentService = new PaymentService(utilityService, userDAOImpl, paymentDAOImpl, lockManager);
        sce.getServletContext().setAttribute("paymentService", paymentService);

        purchaseRollbackScheduler = new PurchaseRollbackScheduler(10*1000, 60*1000, paymentDAOImpl, productDAOImpl);
        purchaseRollbackScheduler.start();

        reserveRollbackScheduler = new ReserveRollbackScheduler(5*1000, 30*1000, cartDAOImpl, productDAOImpl);
        reserveRollbackScheduler.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Shutting down ...");
        connectionPool.closePool();
        batchLogManager.shutDown();

        purchaseRollbackScheduler.stop();
        reserveRollbackScheduler.stop();

        serializedPersistenceManager.persistData(userDAOImpl);
        serializedPersistenceManager.persistData(productDAOImpl);
    }
}
//TODO: add persistence to all DAOs

//TODO: use the new transaction manager instead of locks everywhere
//TODO: services as interface? get supplied from outside and use dynamic proxy for transactions
//TODO: one big DAO containing the smaller ones just like DB? but still work with the smaller ones

//TODO: reconfigure the filters in web.xml
//TODO: a better filter design: have admin/ and web-user/ endpoints to control their accessible actions

//TODO: DI container, config file and relative path

//TODO: rewrite DB later

//TODO: maybe use response objects for methods to avoid excessive exceptions and dispersed logic