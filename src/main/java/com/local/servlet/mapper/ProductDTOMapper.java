package com.local.servlet.mapper;

import com.local.dao.DAOException;
import com.local.dto.ProductDTO;
import com.local.model.Product;
import com.local.service.productmanagement.ProductManagementService;
import com.local.exception.service.productmanagement.ProductNotFoundException;

public class ProductDTOMapper implements DTOMapper<ProductDTO, Product> {
    private ProductManagementService productManagementService;

    public ProductDTOMapper(ProductManagementService productManagementService) {
        this.productManagementService = productManagementService;
    }

    @Override
    public Product map(ProductDTO productDTO) throws DTOMapperException {
        try{
            Product product = productManagementService.getProductById(productDTO.getId());
            return new Product(product.getId(), product.getName(), product.getPrice(), productDTO.getCount());
        }
        catch(ProductNotFoundException | DAOException e){
            throw new DTOMapperException(e.getMessage(), e);
        }
    }
}
