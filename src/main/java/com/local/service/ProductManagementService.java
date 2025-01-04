package com.local.service;

import com.local.dao.DAOException;
import com.local.dao.ProductDAO;
import com.local.dao.transaction.ManagedTransaction;
import com.local.exception.service.productmanagement.InvalidProductPriceException;
import com.local.exception.service.productmanagement.ProductNotFoundException;
import com.local.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Service
public class ProductManagementService {
    private ProductDAO productDAO;

    @Autowired
    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @ManagedTransaction
    public Product addProduct(Product product) throws InvalidProductPriceException, DAOException {
        if(product.getPrice().compareTo(new BigDecimal(0)) <= 0){
            throw new InvalidProductPriceException("product price can't be non positive", null);
        }
        return productDAO.addProduct(product);
    }

    public Product getProductById(int id) throws ProductNotFoundException, DAOException {
        Product product;
        if((product = productDAO.getProductById(id)) == null){
            throw new ProductNotFoundException("product not found", null);
        }
        return product;
    }

    public List<Product> getAllProducts() throws DAOException{
        return productDAO.getAllProducts();
    }

    public HashMap<String, Integer> GetProductsSortedBySold() throws DAOException{
        return productDAO.getProductsSortedBySold();
    }

    public HashMap<String, Integer> getProductsSortedByAvailable() throws DAOException{
        return productDAO.getProductsSortedByAvailable();
    }
}
