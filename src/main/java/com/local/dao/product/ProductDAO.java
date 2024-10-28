package com.local.dao.product;

import com.local.dao.DAOException;
import com.local.model.Product;

import java.util.Set;

public interface ProductDAO {
    Product addProduct(Product product) throws DAOException;
    void updateProduct(Product product) throws DAOException;
    Product getProductById(int id) throws DAOException;
    Product getProductByName(String name) throws DAOException;
    Set<Product> getAllProducts() throws DAOException;
}

//TODO: rewrite DB later
