package com.local.service;

import com.local.dao.product.ProductDAO;
import com.local.dao.product.ProductDAOException;
import com.local.model.Product;

import java.util.List;

public class ProductManagementService {
    private ProductDAO productDAO;

    public ProductManagementService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public void constraintCheck(Product product) throws ProductManagementServiceException{
        if(product.getCount() <= 0){
            throw new ProductManagementServiceException("product count should be positive", null);
        }
        if(product.getPrice() <= 0){
            throw new ProductManagementServiceException("product price should be positive", null);
        }
    }

    public void addProduct(Product product) throws ProductManagementServiceException {
        constraintCheck(product);
        try {
            if(productDAO.findProductByName(product.getName()) != null){
                throw new ProductManagementServiceException("duplicate product name not allowed", null);
            }
            productDAO.addProduct(product);
        } catch (ProductDAOException e) {
            throw new ProductManagementServiceException(e.getMessage(), e);
        }
    }

    public List<Product> getAllProducts() throws ProductManagementServiceException{
        try {
            return productDAO.findAllProducts();
        } catch (ProductDAOException e) {
            throw new ProductManagementServiceException(e.getMessage(), e);
        }
    }
}
//TODO: specific exception naming
