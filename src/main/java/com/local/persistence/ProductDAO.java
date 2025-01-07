package com.local.persistence;

import com.local.dto.ProductReportDTO;
import com.local.model.Product;
import com.local.model.ProductStatus;

import java.util.List;

public interface ProductDAO {
    Product addProduct(Product product) throws DAOException;
    void updateProduct(Product product) throws DAOException;
    Product getProductById(int id) throws DAOException;
    Product getProductByName(String name) throws DAOException;
    List<Product> getAllProducts() throws DAOException;
    List<ProductReportDTO> getProductsSortedByStatus(ProductStatus productStatus) throws DAOException;
}
