package com.local.service.productmanagement;

import com.local.dao.DAOException;
import com.local.dao.cart.CartDAO;
import com.local.dao.product.ProductDAO;
import com.local.exception.service.productmanagement.DuplicateProductNameException;
import com.local.exception.service.productmanagement.InvalidProductCountException;
import com.local.exception.service.productmanagement.InvalidProductPriceException;
import com.local.exception.service.productmanagement.ProductNotFoundException;
import com.local.model.Product;
import com.local.util.lock.LockManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.locks.ReentrantLock;

public class ProductManagementService {
    private ProductDAO productDAO;
    private LockManager lockManager;

    public ProductManagementService(ProductDAO productDAO, LockManager lockManager) {
        this.productDAO = productDAO;
        this.lockManager = lockManager;
    }

    public Product addProduct(Product product) throws InvalidProductCountException, InvalidProductPriceException, DuplicateProductNameException, DAOException {
        if(product.getCount() < 0){
            throw new InvalidProductCountException("product count can't be negative", null);
        }
        if(product.getPrice().compareTo(new BigDecimal(0)) <= 0){
            throw new InvalidProductPriceException("product price can't be non positive", null);
        }
        String name = product.getName();
        ReentrantLock lock = lockManager.getLock(Product.class, name);
        lock.lock();
        try{
            if(productDAO.getProductByName(product.getName()) != null){
                throw new DuplicateProductNameException("duplicate product name not allowed", null);
            }
            return productDAO.addProduct(product);
        }
        finally {
            lock.unlock();
        }
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

    public ArrayList<Product> getProductsSortedBySells() throws DAOException{
        return productDAO.getProductsSortedBySells();
    }

    public ArrayList<Product> getProductsSortedByCount() throws DAOException{
        return productDAO.getProductsSortedByCount();
    }
}
