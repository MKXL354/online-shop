package com.local.model;

import java.math.BigDecimal;

public class LaptopProduct extends Product{
    public LaptopProduct(int id, String name, BigDecimal price, ProductType type, ProductStatus status){
        super(id, name, price, type, status);
    }

    public LaptopProduct(LaptopProduct laptopProduct){
        this(laptopProduct.getId(), laptopProduct.getName(), laptopProduct.getPrice(), laptopProduct.getProductType(), laptopProduct.getProductStatus());
    }
}
