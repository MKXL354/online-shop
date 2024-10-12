package com.local.dao.product;

import com.local.model.Product;

import java.util.List;

public interface ProductDAO {
    void addProduct(Product product) throws ProductDAOException;
    void updateProduct(Product product) throws ProductDAOException;
    void deleteProduct(int id) throws ProductDAOException;
    Product findProductById(int id) throws ProductDAOException;
    Product findProductByName(String name) throws ProductDAOException;
    List<Product> findAllProducts() throws ProductDAOException;
}
