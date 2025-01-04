package com.local.controller;

import com.local.dao.DAOException;
import com.local.exception.service.productmanagement.InvalidProductPriceException;
import com.local.model.Product;
import com.local.service.ProductManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/products")
public class ProductController {
    private ProductManagementService productManagementService;

    @Autowired
    public void setProductManagementService(ProductManagementService productManagementService) {
        this.productManagementService = productManagementService;
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) throws DAOException, InvalidProductPriceException {
        return ResponseEntity.ok(productManagementService.addProduct(product));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() throws DAOException {
        return ResponseEntity.ok(productManagementService.getAllProducts());
    }
}
//TODO: substitute Set with List and Map with a List of Holder Objects to adhere to conventions
