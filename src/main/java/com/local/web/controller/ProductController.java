package com.local.web.controller;

import com.local.dto.ProductDto;
import com.local.dto.ProductReportDto;
import com.local.entity.Product;
import com.local.entity.ProductStatus;
import com.local.entity.UserType;
import com.local.exception.service.productmanagement.ProductNotFoundException;
import com.local.service.ProductManagementService;
import com.local.web.auth.AuthRequired;
import jakarta.validation.Valid;
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
    public ResponseEntity<ProductDto> addProduct(@Valid @RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productManagementService.addProduct(productDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) throws ProductNotFoundException {
        return ResponseEntity.ok(productManagementService.getProductById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(productManagementService.getAllProducts());
    }

    @GetMapping("/report")
    public ResponseEntity<List<ProductReportDto>> getProductSortedByStatus(@RequestParam ProductStatus status) {
        return ResponseEntity.ok(productManagementService.GetProductsSortedByStatus(status));
    }
}
