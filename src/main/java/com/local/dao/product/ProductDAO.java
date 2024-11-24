package com.local.dao.product;

import com.local.dao.DAOException;
import com.local.model.Product;

import java.util.ArrayList;
import java.util.HashSet;

public interface ProductDAO {
    Product addProduct(Product product) throws DAOException;
    void updateProduct(Product product) throws DAOException;
    Product getProductById(int id) throws DAOException;
    HashSet<Product> getAllProducts() throws DAOException;
    ArrayList<Product> getProductsSortedBySells() throws DAOException;
    ArrayList<Product> getProductsSortedByCount() throws DAOException;
}
