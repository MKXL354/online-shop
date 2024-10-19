package com.local.service.user;

import com.local.dao.DAOException;
import com.local.dao.cart.CartDAO;
import com.local.dao.product.ProductDAO;
import com.local.model.Cart;
import com.local.model.Product;
import com.local.model.User;
import com.local.service.productmanagement.NonPositiveProductCountException;
import com.local.service.productmanagement.ProductNotFoundException;

public class UserService {
    private CartDAO cartDAO;
    private ProductDAO productDAO;

    public UserService(CartDAO cartDAO, ProductDAO productDAO) {
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
    }

    private void constraintCheck(Product product) throws NonPositiveProductCountException, ProductNotFoundException, DAOException {
        if(productDAO.getProductById(product.getId()) == null){
            throw new ProductNotFoundException("product not found", null);
        }
        if(product.getCount() <= 0){
            throw new NonPositiveProductCountException("product count should be positive", null);
        }
    }

    public void addProductToCart(Cart cart, Product product) throws NonPositiveProductCountException, ProductNotFoundException, DAOException {
        constraintCheck(product);
        if(cartDAO.getProductInCartById(cart.getId(), product.getId()) == null){
            cartDAO.addProductToCart(cart, product);
        }
        else{
            cartDAO.updateProductInCart(cart, product);
        }
    }

    public Cart getActiveCart(User user) throws DAOException{
        return cartDAO.getActiveCart(user);
    }
}
//TODO: maybe use other services instead of DAO (leads to service dependency but concentration of logic)?
