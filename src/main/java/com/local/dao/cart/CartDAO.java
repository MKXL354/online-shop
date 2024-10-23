package com.local.dao.cart;

import com.local.dao.DAOException;
import com.local.model.Cart;
import com.local.model.Product;
import com.local.model.User;

import java.util.Set;

public interface CartDAO {
    Product getProductInCartById(int cartId, int productId) throws DAOException;
    void addProductToCart(Cart cart, Product product) throws DAOException;
    void updateProductInCart(Cart cart, Product product) throws DAOException;
    void removeProductFromCart(Cart cart, Product product) throws DAOException;
    Cart getActiveCart(User user) throws DAOException;
    Cart addCartToUser(User user) throws DAOException;
    Set<Product> getProductsInCart(int cartId) throws DAOException;
    void purchaseCart(Cart cart) throws DAOException;
    void rollbackCart(Cart cart) throws DAOException;
}
//TODO: use atomic dao methods not the straight complex query (transaction problem)?
//TODO: use ConcurrentHashMap methods like compute or merge for thread-safe updates
//TODO: you might wrap objects in AtomicReference to safely swap them out
//TODO: is immutability worth it? no -> immutable core(like id) - mutable properties that change(like name)
//TODO: catastrophic TransactionException not caught and logged for admin
//TODO: services as interfaces
//TODO: userService depending on a paymentService

//TODO: solution for order concurrency: lock map inside service
//TODO: wallet payment inside payment service

//TODO: IoC container, config file and relative path

//TODO: common service for constraint check
//TODO: abstract services

//TODO: cartDAO: addCart, getActive, addPr, RemPr, upPrCount

//TODO: initialize dao inside memory classes?

//TODO: review DB later