package com.local.service;

import com.local.dto.ProductDto;
import com.local.dto.ProductReportDto;
import com.local.entity.Product;
import com.local.entity.ProductStatus;
import com.local.exception.service.productmanagement.ProductNotFoundException;
import com.local.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductManagementService {
    private ProductRepo productRepo;

    @Autowired
    public void setProductRepo(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public ProductDto addProduct(ProductDto productDto) {
        Product product = productRepo.save(new Product(productDto.getName(), productDto.getPrice(), productDto.getProductType()));
        productDto.setId(product.getId());
        return productDto;
    }

    public Product getProductById(long id) throws ProductNotFoundException {
        return productRepo.findById(id).orElseThrow(() -> new ProductNotFoundException("product not found", null));
    }

    public List<ProductDto> getAllProducts() {
        return productRepo.findAllDto();
    }

    public List<ProductReportDto> GetProductsSortedByStatus(ProductStatus productStatus) {
        return productRepo.findAllByProductStatus(productStatus);
    }
}
