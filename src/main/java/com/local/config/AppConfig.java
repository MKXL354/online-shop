package com.local.config;

import com.local.dao.CartDAO;
import com.local.dao.PaymentDAO;
import com.local.dao.ProductDAO;
import com.local.dao.UserDAO;
import com.local.dao.db.dbconnector.ConnectionPool;
import com.local.dao.db.dbconnector.H2ConnectionPool;
import com.local.dao.factory.DAOFactory;
import com.local.dao.factory.DAOType;
import com.local.dao.factory.DAOTypeFactory;
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
import com.local.service.usermanagement.UserManagementService;
import com.local.service.usermanagement.UserManagementServiceImpl;
import com.local.util.logging.LogManager;
import com.local.util.password.PasswordEncryptor;
import com.local.util.password.PasswordEncryptorImpl;
import com.local.util.token.JwtManager;
import com.local.util.token.TokenManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.lang.reflect.Proxy;

@Configuration
@PropertySource("classpath:db.properties")
@PropertySource("classpath:tokenManager.properties")
@PropertySource("classpath:errorResponse.properties")
@PropertySource("classpath:log.properties")
@PropertySource("classpath:passwordEncryptor.properties")
@PropertySource("classpath:task.properties")
public class AppConfig {
    @Bean
    public ConnectionPool connectionPool() {
        return new H2ConnectionPool();
    }

    @Bean
    public LogManager logManager(){
        return LogManager.getInstance();
    }

    @Bean
    public TokenManager tokenManager() {
        return new JwtManager();
    }

    @Bean
    public PasswordEncryptor passwordEncryptor(){
        return new PasswordEncryptorImpl();
    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new TaskScheduler();
    }

    @Bean
    public TransactionManager transactionManager() {
        return new DBTransactionManager();
    }

    @Bean
    public DAOTypeFactory daoTypeFactory(){
        return new DAOTypeFactory();
    }

    @Bean
    public DAOFactory daoFactory(){
        return daoTypeFactory().getFactory(DAOType.DB, transactionManager());
    }

    @Bean
    public UserDAO userDAOImpl(){
        return daoFactory().getUserDAO();
    }

    @Bean
    public ProductDAO productDAOImpl() {
        return daoFactory().getProductDAO();
    }

    @Bean
    public CartDAO cartDAOImpl() {
        return daoFactory().getCartDAO();
    }

    @Bean
    public PaymentDAO paymentDAOImpl() {
        return daoFactory().getPaymentDAO();
    }

    @Bean
    public CommonService commonServiceImpl(){
        CommonService commonService = new CommonServiceImpl(userDAOImpl(), cartDAOImpl(), paymentDAOImpl());
        return (CommonService) Proxy.newProxyInstance(CommonService.class.getClassLoader(), new Class[]{CommonService.class}, new DBTransactionProxy(commonService, transactionManager()));
    }

    @Bean
    public ProductManagementService productManagementService(){
        ProductManagementService productManagementService = new ProductManagementServiceImpl(productDAOImpl());
        return (ProductManagementService) Proxy.newProxyInstance(ProductManagementService.class.getClassLoader(), new Class[]{ProductManagementService.class}, new DBTransactionProxy(productManagementService, transactionManager()));
    }

    @Bean
    public UserManagementService userManagementService(){
        UserManagementService userManagementService = new UserManagementServiceImpl(userDAOImpl(), passwordEncryptor());
        return (UserManagementService) Proxy.newProxyInstance(UserManagementService.class.getClassLoader(), new Class[]{UserManagementService.class}, new DBTransactionProxy(userManagementService, transactionManager()));
    }

    @Bean
    public PaymentService paymentService(){
        PaymentService paymentService = new PaymentServiceImpl(commonServiceImpl(), userDAOImpl(), paymentDAOImpl());
        return (PaymentService) Proxy.newProxyInstance(PaymentService.class.getClassLoader(), new Class[]{PaymentService.class}, new DBTransactionProxy(paymentService, transactionManager()));
    }

    @Bean
    public UserService userService(){
        UserService userService = new UserServiceImpl(commonServiceImpl(), taskScheduler(), cartDAOImpl(), productDAOImpl(), paymentDAOImpl());
        UserService userServiceProxy = (UserService) Proxy.newProxyInstance(UserService.class.getClassLoader(), new Class[]{UserService.class}, new DBTransactionProxy(userService, transactionManager()));
        userService.setProxy(userServiceProxy);
        return userServiceProxy;
    }
}
//TODO: LogManager Singleton and LogObjects dependency resolved by Spring?
//TODO: fully annotated DI
//TODO: AOP and automatic proxy
