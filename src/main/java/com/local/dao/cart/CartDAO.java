package com.local.dao.cart;

import com.local.dao.DAOException;
import com.local.model.Cart;
import com.local.model.Product;
import com.local.model.User;

import java.util.Set;

public interface CartDAO {
    Cart getActiveCart(User user) throws DAOException;
    Cart addCartToUser(User user) throws DAOException;
    void updateCart(Cart cart) throws DAOException;
    Product getProductInCartById(int cartId, int productId) throws DAOException;
    Set<Product> getProductsInCart(int cartId) throws DAOException;
    void addProductToCart(Cart cart, Product product) throws DAOException;
    void updateProductInCart(Cart cart, Product product) throws DAOException;
    void removeProductFromCart(Cart cart, Product product) throws DAOException;
}

//TODO: updateCart() usage? product already has specialized updating methods