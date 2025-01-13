package com.local.service;

import com.local.dto.ProductReportDto;
import com.local.entity.Product;
import com.local.entity.ProductStatus;
import com.local.exception.service.productmanagement.InvalidProductPriceException;
import com.local.exception.service.productmanagement.ProductNotFoundException;
import com.local.persistence.DAOException;
import com.local.persistence.ProductDAO;
import com.local.persistence.transaction.ManagedTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        if (product.getPrice().compareTo(new BigDecimal(0)) <= 0) {
            throw new InvalidProductPriceException("product price can't be non positive", null);
        }
        return productDAO.addProduct(product);
    }

    public Product getProductById(int id) throws ProductNotFoundException, DAOException {
        Product product;
        if ((product = productDAO.getProductById(id)) == null) {
            throw new ProductNotFoundException("product not found", null);
        }
        return product;
    }

    public List<Product> getAllProducts() throws DAOException {
        return productDAO.getAllProducts();
    }

    public List<ProductReportDto> GetProductsSortedByStatus(ProductStatus productStatus) throws DAOException {
        return productDAO.getProductsSortedByStatus(productStatus);
    }
}
