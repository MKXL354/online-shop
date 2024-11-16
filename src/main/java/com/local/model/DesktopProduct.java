package com.local.model;

import java.math.BigDecimal;

public class DesktopProduct extends Product{
    public DesktopProduct(int id, String name, BigDecimal price, int count, ProductType type){
        super(id, name, price, count, type);
    }

    public DesktopProduct(DesktopProduct desktopProduct){
        this(desktopProduct.getId(), desktopProduct.getName(), desktopProduct.getPrice(), desktopProduct.getCount(), desktopProduct.getType());
    }
}
