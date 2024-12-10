package com.local;

import com.local.dao.factory.DAOFactory;
import com.local.dao.factory.DAOType;
import com.local.dao.CartDAO;
import com.local.dao.PaymentDAO;
import com.local.dao.factory.DAOTypeFactory;
import com.local.dao.ProductDAO;
import com.local.dao.transaction.TransactionManager;
import com.local.dao.transaction.db.DBTransactionManager;
import com.local.dao.transaction.db.DBTransactionProxy;
import com.local.service.common.CommonService;
import com.local.service.common.CommonServiceImpl;
import com.local.service.payment.PaymentService;
import com.local.service.payment.PaymentServiceImpl;
import com.local.service.productmanagement.ProductManagementService;
import com.local.service.productmanagement.ProductManagementServiceImpl;
import com.local.service.scheduler.TaskScheduler;
import com.local.service.user.UserService;
import com.local.service.user.UserServiceImpl;
import com.local.service.usermanagement.UserManagementServiceImpl;
import com.local.util.objectvalidator.ObjectValidator;
import com.local.util.password.PasswordEncryptor;
import com.local.util.password.PasswordEncryptorImpl;
import com.local.dao.UserDAO;
import com.local.dao.db.dbconnector.ConnectionPool;
import com.local.dao.db.dbconnector.H2ConnectionPool;
import com.local.service.usermanagement.UserManagementService;
import com.local.servlet.common.CommonWebComponentService;
import com.local.util.token.JwtManager;
import com.local.util.token.TokenManager;
import com.local.util.PropertyManager;
import com.local.util.logging.BatchLogManager;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.lang.reflect.Proxy;

public class Bootstrap implements ServletContextListener {
    private ConnectionPool connectionPool;
    private BatchLogManager batchLogManager;
    private TaskScheduler taskScheduler;

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

        DAOTypeFactory daoTypeFactory = new DAOTypeFactory();
        TransactionManager transactionManager = new DBTransactionManager(connectionPool);
        DAOFactory daoFactory = daoTypeFactory.getDbFactory(DAOType.DB, transactionManager);
        UserDAO userDAOImpl = daoFactory.getUserDAO();
        ProductDAO productDAOImpl = daoFactory.getProductDAO();
        CartDAO cartDAOImpl = daoFactory.getCartDAO();
        PaymentDAO paymentDAOImpl = daoFactory.getPaymentDAO();

        CommonService commonService = new CommonServiceImpl(userDAOImpl, cartDAOImpl, paymentDAOImpl);
        CommonService proxyCommonService = (CommonService)Proxy.newProxyInstance(CommonService.class.getClassLoader(), new Class[]{CommonService.class}, new DBTransactionProxy(commonService, transactionManager));
        sce.getServletContext().setAttribute("commonService", proxyCommonService);

        PasswordEncryptor passwordEncryptorImpl = new PasswordEncryptorImpl(1000, 32, 256);
        UserManagementService userManagementService = new UserManagementServiceImpl(userDAOImpl, passwordEncryptorImpl);
        UserManagementService proxyUserManagementService = (UserManagementService)Proxy.newProxyInstance(UserManagementService.class.getClassLoader(), new Class[]{UserManagementService.class}, new DBTransactionProxy(userManagementService, transactionManager));
        sce.getServletContext().setAttribute("userManagementService", proxyUserManagementService);

        ProductManagementService productManagementService = new ProductManagementServiceImpl(productDAOImpl);
        ProductManagementService proxyProductManagementService = (ProductManagementService)Proxy.newProxyInstance(ProductManagementService.class.getClassLoader(), new Class[]{ProductManagementService.class}, new DBTransactionProxy(productManagementService, transactionManager));
        sce.getServletContext().setAttribute("productManagementService", proxyProductManagementService);

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

        taskScheduler = new TaskScheduler(16);
        taskScheduler.start();

        UserService userServiceImpl = new UserServiceImpl(commonService, taskScheduler, cartDAOImpl, productDAOImpl, paymentDAOImpl);
        UserService proxyUserService = (UserService)Proxy.newProxyInstance(UserService.class.getClassLoader(), new Class[]{UserService.class}, new DBTransactionProxy(userServiceImpl, transactionManager));
        userServiceImpl.setProxy(proxyUserService);
        sce.getServletContext().setAttribute("userService", proxyUserService);

        PaymentService paymentService = new PaymentServiceImpl(commonService, userDAOImpl, paymentDAOImpl);
        PaymentService proxyPaymentService = (PaymentService)Proxy.newProxyInstance(PaymentService.class.getClassLoader(), new Class[]{PaymentService.class}, new DBTransactionProxy(paymentService, transactionManager));
        sce.getServletContext().setAttribute("paymentService", proxyPaymentService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Shutting down ...");
        batchLogManager.shutDown();
        taskScheduler.stop();
        connectionPool.closePool();
    }
}


//TODO: apply Custom AutoClose inside all DBDAOs? -> remove closeConnection() of TransactionManager
//TODO: one catch block inside DBDAO methods?

//TODO: service exceptions being thrown up instead of many smaller ones?

//TODO: DAO to work with ids too(like service)? unified layers, less get methods, faster

//TODO: change structure of logs? internal log manager(new Log().submit()), logs for activity, exception etc.

//TODO: better structure of DB select queries? a fluent, table/DAO specific query constructor?

//TODO: status enums keeping generic state -> one enums class being used and one DB table

//TODO: lazy fetch on aggregates(builder inside commonService)? like Cart and User for Payment
//TODO: send model DTO over the web not the full object

//TODO: annotation for filter authentication? like an annotation on servlet: @RequiresAuth?

//TODO: maybe use generic response objects for services to avoid excessive exceptions and dispersed logic

//TODO: DI container, config file and relative path

//TODO: persistence/rewrite/transaction(read-update, insertion, rollback) for all MemDAOs?

//TODO: access MemDAO like DB? get a connection and delegate to a custom composed collection
//TODO: custom locked collection -> so the locking doesn't have to repeat in every DAO method