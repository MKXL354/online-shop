package com.local.service;

import com.local.dto.CartDto;
import com.local.entity.*;
import com.local.exception.service.payment.PendingPaymentNotFoundException;
import com.local.exception.service.productmanagement.ProductNotFoundException;
import com.local.exception.service.user.EmptyCartException;
import com.local.exception.service.user.PreviousPaymentPendingException;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.repository.CartRepo;
import com.local.repository.PaymentRepo;
import com.local.repository.ProductRepo;
import com.local.scheduler.TaskScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    private UserService proxy;
    private CommonService commonService;
    private TaskScheduler taskScheduler;
    private CartRepo cartRepo;
    private ProductRepo productRepo;
    private PaymentRepo paymentRepo;

    @Autowired
    public void setProxy(UserService proxy) {
        this.proxy = proxy;
    }

    @Autowired
    public void setCommonService(CommonService commonService) {
        this.commonService = commonService;
    }

    @Autowired
    public void setTaskScheduler(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    @Autowired
    public void setCartRepo(CartRepo cartRepo) {
        this.cartRepo = cartRepo;
    }

    @Autowired
    public void setProductRepo(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @Autowired
    public void setPaymentRepo(PaymentRepo paymentRepo) {
        this.paymentRepo = paymentRepo;
    }

    public Cart getActiveCart(long userId) throws UserNotFoundException {
        Optional<Cart> cart;
        if((cart = cartRepo.findByAppUserIdAndCartStatus(userId, CartStatus.ACTIVE)).isPresent()){
            return cart.get();
        }
        return cartRepo.save(new Cart(commonService.getUserById(userId)));
    }
    
    public CartDto getActiveCartDto(long userId) throws UserNotFoundException {
        return new CartDto(getActiveCart(userId));
    }
    
    public void addProductToCart(long userId, String productName) throws UserNotFoundException, PreviousPaymentPendingException, ProductNotFoundException {
        if (paymentRepo.findPaymentByAppUserIdAndPaymentStatus(userId, PaymentStatus.PENDING).isPresent()) {
            throw new PreviousPaymentPendingException("a previous payment is pending", null);
        }
        Product product = productRepo.findByNameAndProductStatus(productName, ProductStatus.AVAILABLE).orElseThrow(() -> new ProductNotFoundException("product not found", null));
        product.setProductStatus(ProductStatus.RESERVED);
        productRepo.save(product);

        Cart cart = getActiveCart(userId);
        cart.getProducts().put(product.getId(), product);
        cart.setLastUpdateTime(LocalDateTime.now());
        cartRepo.save(cart);

        taskScheduler.submitTask(() -> {
            proxy.removeProductFromCart(userId, product.getId());
            return null;
        }, 10 * 60 * 1000);
    }
    
    public void removeProductFromCart(long userId, long productId) throws UserNotFoundException, ProductNotFoundException {
        Cart cart = getActiveCart(userId);
        Product product = cart.getProducts().remove(productId);
        if (product == null) {
            throw new ProductNotFoundException("product not found in cart", null);
        }
        cart.setLastUpdateTime(LocalDateTime.now());
        cartRepo.save(cart);

        product.setProductStatus(ProductStatus.AVAILABLE);
        productRepo.save(product);
    }
    
    public void finalizePurchase(long userId) throws UserNotFoundException, EmptyCartException {
        AppUser user = commonService.getUserById(userId);
        Cart cart = getActiveCart(userId);
        Map<Long, Product> cartProducts = cart.getProducts();
        if (cartProducts.isEmpty()) {
            throw new EmptyCartException("user cart is empty", null);
        }

        BigDecimal totalPrice = new BigDecimal(0);
        for (Product product : cartProducts.values()) {
            totalPrice = totalPrice.add(product.getPrice());
            product.setProductStatus(ProductStatus.SOLD);
            productRepo.save(product);
        }
        LocalDateTime now = LocalDateTime.now();
        Payment payment = new Payment(user, cart, totalPrice, now, PaymentStatus.PENDING);
        paymentRepo.save(payment);

        cart.setLastUpdateTime(now);
        cart.setCartStatus(CartStatus.PROCESSED);
        cartRepo.save(cart);
        taskScheduler.submitTask(() -> {
            proxy.cancelPurchase(userId);
            return null;
        }, 5 * 60 * 1000);
    }

    public void cancelPurchase(long userId) throws UserNotFoundException, PendingPaymentNotFoundException {
        AppUser user = commonService.getUserById(userId);
        Payment payment = paymentRepo.findPaymentByAppUserIdAndPaymentStatus(userId, PaymentStatus.PENDING).orElseThrow(() -> new PendingPaymentNotFoundException("no active payment found", null));
        Cart cart = payment.getCart();
        for (Product product : cart.getProducts().values()) {
            product.setProductStatus(ProductStatus.AVAILABLE);
            productRepo.save(product);
        }
        LocalDateTime now = LocalDateTime.now();

        cart.setCartStatus(CartStatus.CANCELLED);
        cart.setLastUpdateTime(now);
        cartRepo.save(cart);

        payment.setPaymentStatus(PaymentStatus.FAILED);
        payment.setLastUpdateTime(now);
        paymentRepo.save(payment);
    }
}
