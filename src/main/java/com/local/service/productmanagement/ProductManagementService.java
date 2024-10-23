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

    private void constraintCheck(Product product) throws NegativeProductCountException, NonPositiveProductPriceException {
        if(product.getCount() < 0){
            throw new NegativeProductCountException("product count cant be negative", null);
        }
        if(product.getPrice() <= 0){
            throw new NonPositiveProductPriceException("product price cant be non positive", null);
        }
    }

    public void addProduct(Product product) throws NegativeProductCountException, NonPositiveProductPriceException, DuplicateProductNameException, DAOException {
        constraintCheck(product);
        String name = product.getName();
        ReentrantLock lock = productLocks.computeIfAbsent(name, (n) -> new ReentrantLock());
        lock.lock();
        try{
            if(productDAO.getProductByName(product.getName()) != null){
                throw new DuplicateProductNameException("duplicate product name not allowed", null);
            }
            productDAO.addProduct(product);
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
