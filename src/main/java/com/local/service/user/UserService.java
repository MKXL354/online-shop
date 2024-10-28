package com.local.service.user;

import com.local.dao.DAOException;
import com.local.dao.cart.CartDAO;
import com.local.dao.product.ProductDAO;
import com.local.dao.user.UserDAO;
import com.local.model.Cart;
import com.local.model.Product;
import com.local.model.User;
import com.local.service.TransactionException;
import com.local.service.productmanagement.InvalidProductCountException;
import com.local.service.productmanagement.ProductNotFoundException;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class UserService {
    private CartDAO cartDAO;
    private ProductDAO productDAO;
    private UserDAO userDAO;
    private ConcurrentHashMap<Integer, ReentrantLock> cartLocks;

    public UserService(CartDAO cartDAO, ProductDAO productDAO, UserDAO userDAO) {
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
        this.userDAO = userDAO;
        this.cartLocks = new ConcurrentHashMap<>();
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
        ReentrantLock lock = cartLocks.computeIfAbsent(cart.getId(), (c) -> new ReentrantLock());
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

    public synchronized void purchase(User user) throws EmptyCartException, InsufficientBalanceException, InsufficientProductCountException, TransactionException, DAOException {
//        TODO: test synchronization then implement better locking (reentrantLock based on id)
        Cart cart = getCart(user);
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
        user.setBalance(user.getBalance() - totalPrice);

        Product mainProduct;
        for(Product orderedProduct : cartProducts){
            mainProduct = productDAO.getProductById(orderedProduct.getId());
            if(mainProduct.getCount() < orderedProduct.getCount()){
                throw new InsufficientProductCountException("ordered count is higher than available count", null);
            }
            orderedProduct.setCount(mainProduct.getCount() - orderedProduct.getCount());
        }

        try{
            userDAO.updateUser(user);
            for(Product product: cartProducts){
                productDAO.updateProduct(product);
            }
        }
        catch(DAOException e){
            throw new TransactionException("catastrophic transaction exception occurred", null);
        }
    }
}

//TODO: ideas of a transaction manager for safer service transactions everywhere
