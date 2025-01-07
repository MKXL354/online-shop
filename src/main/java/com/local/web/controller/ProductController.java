package com.local.web.controller;

import com.local.dto.ProductReportDTO;
import com.local.model.ProductStatus;
import com.local.persistence.DAOException;
import com.local.exception.service.productmanagement.InvalidProductPriceException;
import com.local.model.Product;
import com.local.model.UserType;
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
    public ResponseEntity<Product> addProduct(@RequestBody Product product) throws DAOException, InvalidProductPriceException {
        return ResponseEntity.ok(productManagementService.addProduct(product));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() throws DAOException {
        return ResponseEntity.ok(productManagementService.getAllProducts());
    }

    @GetMapping("/report")
    public ResponseEntity<List<ProductReportDTO>> getProductSortedByStatus(@RequestParam ProductStatus status) throws DAOException {
        return ResponseEntity.ok(productManagementService.GetProductsSortedByStatus(status));
    }
}
