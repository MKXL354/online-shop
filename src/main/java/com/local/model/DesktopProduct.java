package com.local.model;

import java.math.BigDecimal;

public class DesktopProduct extends Product{
    public DesktopProduct(int id, String name, BigDecimal price, ProductType type, ProductStatus status){
        super(id, name, price, type, status);
    }

    public DesktopProduct(DesktopProduct desktopProduct){
        this(desktopProduct.getId(), desktopProduct.getName(), desktopProduct.getPrice(), desktopProduct.getType(), desktopProduct.getStatus());
    }
}
