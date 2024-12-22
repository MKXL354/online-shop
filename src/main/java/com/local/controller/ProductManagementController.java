package com.local.controller;

import com.local.dao.DAOException;
import com.local.exception.service.productmanagement.InvalidProductPriceException;
import com.local.model.Product;
import com.local.service.productmanagement.ProductManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/products")
public class ProductManagementController {
    private ProductManagementService productManagementService;

    @Autowired
    public void setProductManagementService(ProductManagementService productManagementService) {
        this.productManagementService = productManagementService;
    }

    @PostMapping
    public Product addProduct(@RequestBody Product product) throws DAOException, InvalidProductPriceException {
        return productManagementService.addProduct(product);
    }

    @GetMapping
    public Set<Product> getAllProducts() throws DAOException {
        return productManagementService.getAllProducts();
    }
}
//TODO: substitute Set with List and Map with a List of Holder Objects to adhere to conventions
