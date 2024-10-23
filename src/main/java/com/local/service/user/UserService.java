package com.local.service.user;

import com.local.dao.DAOException;
import com.local.dao.cart.CartDAO;
import com.local.dao.product.ProductDAO;
import com.local.model.Cart;
import com.local.model.Product;
import com.local.model.User;
import com.local.service.productmanagement.InvalidProductCountException;
import com.local.service.productmanagement.ProductNotFoundException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class UserService {
    private CartDAO cartDAO;
    private ProductDAO productDAO;
    private ConcurrentHashMap<Integer, ReentrantLock> cartLocks;

    public UserService(CartDAO cartDAO, ProductDAO productDAO) {
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
        this.cartLocks = new ConcurrentHashMap<>();
    }

    public Cart getCart(User user) throws DAOException{
        Cart cart;
        if((cart = cartDAO.getActiveCart(user)) == null){
            return cartDAO.addCartToUser(user);
        }
        return cart;
    }

    public void addProductToCart(Cart cart, Product product) throws InvalidProductCountException, ProductNotFoundException, DAOException {
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
                    throw new InvalidProductCountException("product count can't be non negative", null);
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

//    public void purchaseCart(Cart cart) throws DAOException {
    //TODO: throw catastrophic TransactionException in case of failure: not caught and logged for admin
//    }
}
