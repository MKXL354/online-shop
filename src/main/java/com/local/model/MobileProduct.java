package com.local.model;

import java.math.BigDecimal;

public class MobileProduct extends Product{
    public MobileProduct(int id, String name, BigDecimal price, ProductType type, ProductStatus status){
        super(id, name, price, type, status);
    }

    public MobileProduct(MobileProduct mobileProduct){
        this(mobileProduct.getId(), mobileProduct.getName(), mobileProduct.getPrice(), mobileProduct.getType(), mobileProduct.getStatus());
    }
}
