package com.local.servlet.mapper;

import com.local.dto.ProductDTO;
import com.local.model.Product;
import com.local.service.productmanagement.ProductManagementService;

public class ProductDTOMapper implements DTOMapper<ProductDTO, Product> {
    private ProductManagementService productManagementService;

    public ProductDTOMapper(ProductManagementService productManagementService) {
        this.productManagementService = productManagementService;
    }

    @Override
    public Product map(ProductDTO productDTO) throws DTOMapperException {
//        try{
//            Product product = productManagementService.getProductById(productDTO.getId());
//            product.setCount(productDTO.getCount());
//            return product;
//        }
//        catch(ProductNotFoundException | DAOException e){
//            throw new DTOMapperException(e.getMessage(), e);
//        }
        return null;
    }
}
