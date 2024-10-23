package com.local.service.productmanagement;

import com.local.dao.DAOException;
import com.local.dao.product.ProductDAO;
import com.local.model.Product;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class ProductManagementService {
    private ProductDAO productDAO;
    private ConcurrentHashMap<String, ReentrantLock> productLocks;

    public ProductManagementService(ProductDAO productDAO) {
        this.productDAO = productDAO;
        this.productLocks = new ConcurrentHashMap<>();
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
        ReentrantLock lock = productLocks.computeIfAbsent(name, (n) -> new ReentrantLock());
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

    public List<Product> getAllProducts() throws DAOException{
        return productDAO.getAllProducts();
    }
}
