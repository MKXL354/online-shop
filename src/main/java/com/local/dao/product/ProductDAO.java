package com.local.dao.product;

import com.local.dao.DAOException;
import com.local.model.Product;

import java.util.List;

public interface ProductDAO {
    void addProduct(Product product) throws DAOException;
    void updateProduct(Product product) throws DAOException;
    Product getProductById(int id) throws DAOException;
    Product getProductByName(String name) throws DAOException;
    List<Product> getAllProducts() throws DAOException;
}

//TODO: review DB later
