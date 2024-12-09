package com.local.service.productmanagement;

import com.local.dao.DAOException;
import com.local.dao.ProductDAO;
import com.local.exception.service.productmanagement.InvalidProductPriceException;
import com.local.exception.service.productmanagement.ProductNotFoundException;
import com.local.model.Product;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;

public class ProductManagementServiceImpl implements ProductManagementService{
    private ProductDAO productDAO;

    public ProductManagementServiceImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public Product addProduct(Product product) throws InvalidProductPriceException, DAOException {
        if(product.getPrice().compareTo(new BigDecimal(0)) <= 0){
            throw new InvalidProductPriceException("product price can't be non positive", null);
        }
        return productDAO.addProduct(product);
    }

    @Override
    public Product getProductById(int id) throws ProductNotFoundException, DAOException {
        Product product;
        if((product = productDAO.getProductById(id)) == null){
            throw new ProductNotFoundException("product not found", null);
        }
        return product;
    }

    @Override
    public HashSet<Product> getAllProducts() throws DAOException{
        return productDAO.getAllProducts();
    }

    @Override
    public HashMap<String, Integer> GetProductsSortedBySold() throws DAOException{
        return productDAO.getProductsSortedBySold();
    }

    @Override
    public HashMap<String, Integer> getProductsSortedByAvailable() throws DAOException{
        return productDAO.getProductsSortedByAvailable();
    }
}
