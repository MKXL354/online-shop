package com.local.service.productmanagement;

import com.local.dao.DAOException;
import com.local.dao.transaction.ManagedTransaction;
import com.local.exception.service.productmanagement.InvalidProductPriceException;
import com.local.exception.service.productmanagement.ProductNotFoundException;
import com.local.model.Product;

import java.util.HashMap;
import java.util.HashSet;

public interface ProductManagementService {
    @ManagedTransaction
    Product addProduct(Product product) throws InvalidProductPriceException, DAOException;
    Product getProductById(int id) throws ProductNotFoundException, DAOException;
    HashSet<Product> getAllProducts() throws DAOException;
    HashMap<String, Integer> GetProductsSortedBySold() throws DAOException;
    HashMap<String, Integer> getProductsSortedByAvailable() throws DAOException;
}
