package com.local.dao;

import com.local.model.Cart;
import com.local.model.Product;

import java.util.HashMap;

public interface CartDAO {
    Cart getCartById(int cartId) throws DAOException;
    Cart getActiveCart(int userId) throws DAOException;
    Cart addCartToUser(int userId) throws DAOException;
    void updateCart(Cart cart) throws DAOException;
    void addProductToCart(Cart cart, Product product) throws DAOException;
    void removeProductFromCart(Cart cart, Product product) throws DAOException;
    HashMap<Integer, Product> getProductsInCart(int cartId) throws DAOException;
}
