//package com.local.service;
//
//import com.local.entity.Cart;
//import com.local.entity.Payment;
//import com.local.entity.User;
//import com.local.exception.common.ApplicationRuntimeException;
//import com.local.exception.service.common.CartNotFoundException;
//import com.local.exception.service.usermanagement.UserNotFoundException;
//import com.local.persistence.CartRepo;
//import com.local.persistence.DAOException;
//import com.local.persistence.PaymentDAO;
//import com.local.persistence.UserRepo;
//import com.local.repository.UserRepo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CommonService {
//    private UserRepo userRepo;
//    private CartRepo CartRepo;
//    private PaymentDAO paymentDAO;
//
//    @Autowired
//    public void setUserRepo(UserRepo userRepo) {
//        this.userRepo = userRepo;
//    }
//
//    @Autowired
//    public void setCartRepo(CartRepo CartRepo) {
//        this.CartRepo = CartRepo;
//    }
//
//    @Autowired
//    public void setPaymentDAO(PaymentDAO paymentDAO) {
//        this.paymentDAO = paymentDAO;
//    }
//
//    public User getUserById(int userId) throws UserNotFoundException, DAOException {
//        User user;
//        if ((user = userRepo.getUserById(userId)) == null) {
//            throw new UserNotFoundException("user not found", null);
//        }
//        return user;
//    }
//
//    public Cart getActiveCart(int userId) throws UserNotFoundException, DAOException {
//        Cart cart = CartRepo.getActiveCart(userId);
//        User user = getUserById(userId);
//        if (cart == null) {
//            cart = CartRepo.addCartToUser(userId);
//            cart.setUserId(user);
//            return cart;
//        }
//        cart.setUserId(user);
//        cart.setProducts(CartRepo.getProductsInCart(cart.getId()));
//        return cart;
//    }
//
//    public Cart getCartById(int cartId) throws CartNotFoundException, DAOException {
//        Cart cart = CartRepo.getCartById(cartId);
//        if (cart == null) {
//            throw new CartNotFoundException("cart not found", null);
//        }
//        try {
//            cart.setUserId(getUserById(cart.getUserId().getId()));
//        } catch (UserNotFoundException e) {
//            throw new ApplicationRuntimeException("user associated with cart not found", null);
//        }
//        cart.setProducts(CartRepo.getProductsInCart(cartId));
//        return cart;
//    }
//
//    public Payment getPendingPayment(int userId) throws UserNotFoundException, DAOException {
//        Payment payment = paymentDAO.getPendingPayment(userId);
//        if (payment == null) {
//            return null;
//        }
//        payment.setUserId(getUserById(userId));
//        try {
//            payment.setCart(getCartById(payment.getCart().getId()));
//        } catch (CartNotFoundException e) {
//            throw new ApplicationRuntimeException("cart associated with payment not found", null);
//        }
//        return payment;
//    }
//}
