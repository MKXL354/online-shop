package com.local.persistence;

import com.local.model.Product;

import java.util.LinkedHashMap;
import java.util.List;

public interface ProductDAO {
    Product addProduct(Product product) throws DAOException;
    void updateProduct(Product product) throws DAOException;
    Product getProductById(int id) throws DAOException;
    Product getProductByName(String name) throws DAOException;
    List<Product> getAllProducts() throws DAOException;
    LinkedHashMap<String, Integer> getProductsSortedBySold() throws DAOException;
    LinkedHashMap<String, Integer> getProductsSortedByAvailable() throws DAOException;
}
