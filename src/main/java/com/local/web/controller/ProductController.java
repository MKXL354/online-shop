package com.local.web.controller;

import com.local.dto.ProductDto;
import com.local.dto.ProductReportDto;
import com.local.entity.Product;
import com.local.entity.ProductStatus;
import com.local.entity.UserType;
import com.local.exception.service.productmanagement.InvalidProductPriceException;
import com.local.service.ProductManagementService;
import com.local.web.auth.AuthRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private ProductManagementService productManagementService;

    @Autowired
    public void setProductManagementService(ProductManagementService productManagementService) {
        this.productManagementService = productManagementService;
    }

    @AuthRequired(UserType.ADMIN)
    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody ProductDto productDto) throws InvalidProductPriceException {
        return ResponseEntity.ok(productManagementService.addProduct(productDto));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productManagementService.getAllProducts());
    }

    @GetMapping("/report")
    public ResponseEntity<List<ProductReportDto>> getProductSortedByStatus(@RequestParam ProductStatus status) {
        return ResponseEntity.ok(productManagementService.GetProductsSortedByStatus(status));
    }
}
