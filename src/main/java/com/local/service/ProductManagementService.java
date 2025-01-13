package com.local.service;

import com.local.dto.ProductReportDto;
import com.local.entity.Product;
import com.local.entity.ProductStatus;
import com.local.exception.service.productmanagement.InvalidProductPriceException;
import com.local.exception.service.productmanagement.ProductNotFoundException;
import com.local.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ProductManagementService {
    private ProductRepo productRepo;

    @Autowired
    public void setProductRepo(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public Product addProduct(Product product) throws InvalidProductPriceException {
        if (product.getPrice().compareTo(new BigDecimal(0)) <= 0) {
            throw new InvalidProductPriceException("product price can't be non positive", null);
        }
        return productRepo.save(product);
    }

    public Product getProductById(long id) throws ProductNotFoundException {
        return productRepo.findById(id).orElseThrow(() -> new ProductNotFoundException("product not found", null));
    }

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public List<ProductReportDto> GetProductsSortedByStatus(ProductStatus productStatus) {
        return productRepo.findAllByProductStatus(productStatus);
    }
}
