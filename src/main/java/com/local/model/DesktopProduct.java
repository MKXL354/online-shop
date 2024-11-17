package com.local.model;

import java.math.BigDecimal;

public class DesktopProduct extends Product{
    public DesktopProduct(int id, String name, BigDecimal price, int count, int sold, ProductType type){
        super(id, name, price, count, sold, type);
    }

    public DesktopProduct(DesktopProduct desktopProduct){
        this(desktopProduct.getId(), desktopProduct.getName(), desktopProduct.getPrice(), desktopProduct.getCount(), desktopProduct.getSold(), desktopProduct.getType());
    }
}
