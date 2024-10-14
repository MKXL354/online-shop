package com.local.dao.product;

import com.local.dao.DAOException;
import com.local.model.Product;

import java.util.List;

public interface ProductDAO {
    void addProduct(Product product) throws DAOException;
    void updateProduct(Product product) throws DAOException;
    void deleteProduct(int id) throws DAOException;
    Product findProductById(int id) throws DAOException;
    Product findProductByName(String name) throws DAOException;
    List<Product> findAllProducts() throws DAOException;
}
