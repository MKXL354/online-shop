package com.local.dao;

import com.local.model.Product;

import java.util.HashSet;
import java.util.LinkedHashMap;

public interface ProductDAO {
    Product addProduct(Product product) throws DAOException;
    void updateProduct(Product product) throws DAOException;
    Product getProductById(int id) throws DAOException;
    Product getProductByName(String name) throws DAOException;
    HashSet<Product> getAllProducts() throws DAOException;
    LinkedHashMap<String, Integer> getProductsSortedBySold() throws DAOException;
    LinkedHashMap<String, Integer> getProductsSortedByAvailable() throws DAOException;
}
