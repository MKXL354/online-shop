package com.local.model;

import java.math.BigDecimal;

public class MobileProduct extends Product{
    public MobileProduct(int id, String name, BigDecimal price, int count, ProductType type){
        super(id, name, price, count, type);
    }

    public MobileProduct(MobileProduct mobileProduct){
        this(mobileProduct.getId(), mobileProduct.getName(), mobileProduct.getPrice(), mobileProduct.getCount(), mobileProduct.getType());
    }
}
