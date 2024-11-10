package com.local.service.user;

import com.local.dao.DAOException;
import com.local.dao.cart.CartDAO;
import com.local.dao.payment.PaymentDAO;
import com.local.dao.product.ProductDAO;
import com.local.dao.user.UserDAO;
import com.local.model.Cart;
import com.local.model.Payment;
import com.local.model.Product;
import com.local.model.User;
import com.local.service.TransactionException;
import com.local.service.productmanagement.InvalidProductCountException;
import com.local.service.productmanagement.ProductNotFoundException;
import com.local.util.lock.LockManager;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public class UserService {
    private CartDAO cartDAO;
    private ProductDAO productDAO;
    private UserDAO userDAO;
    private PaymentDAO paymentDAO;
    private LockManager lockManager;

    public UserService(CartDAO cartDAO, ProductDAO productDAO, UserDAO userDAO, PaymentDAO paymentDAO, LockManager lockManager) {
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
        this.userDAO = userDAO;
        this.paymentDAO = paymentDAO;
        this.lockManager = lockManager;
    }

    public Cart getCart(User user) throws DAOException{
        Cart cart;
        if((cart = cartDAO.getActiveCart(user)) == null){
            return cartDAO.addCartToUser(user);
        }
        return cart;
    }

    public void addProductToCart(User user, Product product) throws InvalidProductCountException, ProductNotFoundException, DAOException {
        Cart cart = getCart(user);
        if(productDAO.getProductById(product.getId()) == null){
            throw new ProductNotFoundException("product not found", null);
        }
        ReentrantLock lock = lockManager.getLock(Cart.class, cart.getId());
        lock.lock();
        try{
            Product productInCart = cartDAO.getProductInCartById(cart.getId(), product.getId());
            if(productInCart == null){
                if(product.getCount() <= 0){
                    throw new InvalidProductCountException("product count can't be non positive", null);
                }
                cartDAO.addProductToCart(cart, product);
            }
            else{
                if(product.getCount() < 0){
                    throw new InvalidProductCountException("product count can't be non positive", null);
                }
                if(product.getCount() == 0){
                    cartDAO.removeProductFromCart(cart, product);
                }
                cartDAO.updateProductInCart(cart, product);
            }
        }
        finally {
            lock.unlock();
        }
    }

    public void balancePurchase(User user) throws EmptyCartException, InsufficientBalanceException, InsufficientProductCountException, TransactionException, DAOException {
//        TODO: remove the copies (used for controlling the update) or fix it later. immutability?
        Cart cart = getCart(user);
        ReentrantLock cartLock = lockManager.getLock(Cart.class, cart.getId());
        ReentrantLock userLock = lockManager.getLock(User.class, user.getUsername());
        cartLock.lock();
        userLock.lock();
        LinkedList<ReentrantLock> productLocks = new LinkedList<>();
        for(Product product : cart.getProducts()){
            ReentrantLock productLock = lockManager.getLock(Product.class, product.getId());
            productLock.lock();
            productLocks.add(productLock);
        }
        try{
            Set<Product> cartProducts = cart.getProducts();
            if(cartProducts.isEmpty()){
                throw new EmptyCartException("user cart is empty", null);
            }

            double totalPrice = 0;
            for(Product product : cartProducts){
                totalPrice += product.getPrice() * product.getCount();
            }
            if(user.getBalance() < totalPrice){
                throw new InsufficientBalanceException("insufficient account balance", null);
            }
            User userCopy = new User(user.getId(), user.getUsername(), user.getPassword(), user.getType(), user.getBalance());
            userCopy.setBalance(user.getBalance() - totalPrice);
            Payment payment = new Payment(0, userCopy, cart, totalPrice);

            Set<Product> updatedProducts = new HashSet<>();
            Product mainProduct;
            for(Product orderedProduct : cartProducts){
                mainProduct = productDAO.getProductById(orderedProduct.getId());
                if(mainProduct.getCount() < orderedProduct.getCount()){
                    throw new InsufficientProductCountException("ordered count is higher than available count", null);
                }
                Product productCopy = new Product(orderedProduct.getId(), orderedProduct.getName(), orderedProduct.getPrice(), orderedProduct.getCount());
                productCopy.setCount(mainProduct.getCount() - orderedProduct.getCount());
                updatedProducts.add(productCopy);
            }

            try{
                userDAO.updateUser(userCopy);
                paymentDAO.addPayment(payment);
                for(Product product: updatedProducts){
                    productDAO.updateProduct(product);
                }
            }
            catch(DAOException e){
//            TODO: note the catastrophic transaction exception that can leave the system in incorrect state
                throw new TransactionException("catastrophic transaction exception occurred", null);
            }
        }
        finally{
            cartLock.unlock();
            userLock.unlock();
            for(ReentrantLock productLock : productLocks){
                productLock.unlock();
            }
        }
    }
}
