package com.local.service.productmanagement;

import com.local.dao.DAOException;
import com.local.dao.ProductDAO;
import com.local.exception.service.productmanagement.InvalidProductPriceException;
import com.local.exception.service.productmanagement.ProductNotFoundException;
import com.local.model.Product;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;

public class ProductManagementService {
    private ProductDAO productDAO;

    public ProductManagementService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

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

    public HashSet<Product> getAllProducts() throws DAOException{
        return productDAO.getAllProducts();
    }

    public HashMap<String, Integer> getProductsSortedBySells() throws DAOException{
        return productDAO.getProductsSortedBySells();
    }

    public HashMap<String, Integer> getProductsSortedByCount() throws DAOException{
        return productDAO.getProductsSortedByCount();
    }
}
