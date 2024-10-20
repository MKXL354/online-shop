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
    Cart getActiveCart(User user) throws DAOException;
    Cart addCartToUser(User user) throws DAOException;
    Set<Product> getProductsInCart(int cartId) throws DAOException;
}
