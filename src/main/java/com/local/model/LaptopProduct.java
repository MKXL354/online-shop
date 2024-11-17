package com.local.model;

import java.math.BigDecimal;

public class LaptopProduct extends Product{
    public LaptopProduct(int id, String name, BigDecimal price, int count, int sold, ProductType type){
        super(id, name, price, count, sold, type);
    }

    public LaptopProduct(LaptopProduct laptopProduct){
        this(laptopProduct.getId(), laptopProduct.getName(), laptopProduct.getPrice(), laptopProduct.getCount(), laptopProduct.getSold(), laptopProduct.getType());
    }
}
