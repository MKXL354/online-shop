package com.local.service.productmanagement;

import com.local.dao.DAOException;
import com.local.dao.product.ProductDAO;
import com.local.model.Product;

import java.util.List;

public class ProductManagementService {
    private ProductDAO productDAO;

    public ProductManagementService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    private void constraintCheck(Product product) throws NonPositiveProductCountException, NonPositiveProductPriceException {
        if(product.getCount() <= 0){
            throw new NonPositiveProductCountException("product count should be positive", null);
        }
        if(product.getPrice() <= 0){
            throw new NonPositiveProductPriceException("product price should be positive", null);
        }
    }

    public void addProduct(Product product) throws NonPositiveProductCountException, NonPositiveProductPriceException, DuplicateProductNameException, DAOException {
        constraintCheck(product);
        if(productDAO.getProductByName(product.getName()) != null){
            throw new DuplicateProductNameException("duplicate product name not allowed", null);
        }
        productDAO.addProduct(product);
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
