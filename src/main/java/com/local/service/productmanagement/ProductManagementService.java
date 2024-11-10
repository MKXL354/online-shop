package com.local.service.productmanagement;

import com.local.dao.DAOException;
import com.local.dao.product.ProductDAO;
import com.local.model.Product;
import com.local.util.lock.LockManager;

import java.util.HashSet;
import java.util.concurrent.locks.ReentrantLock;

public class ProductManagementService {
    private ProductDAO productDAO;
    private LockManager lockManager;

    public ProductManagementService(ProductDAO productDAO, LockManager lockManager) {
        this.productDAO = productDAO;
        this.lockManager = lockManager;
    }

    private void constraintCheck(Product product) throws InvalidProductCountException, InvalidProductPriceException {
        if(product.getCount() < 0){
            throw new InvalidProductCountException("product count can't be negative", null);
        }
        if(product.getPrice() <= 0){
            throw new InvalidProductPriceException("product price can't be non positive", null);
        }
    }

    public Product addProduct(Product product) throws InvalidProductCountException, InvalidProductPriceException, DuplicateProductNameException, DAOException {
        constraintCheck(product);
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
}
