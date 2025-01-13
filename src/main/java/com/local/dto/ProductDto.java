package com.local.dto;

import com.local.entity.ProductType;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class ProductDto {
    @NotEmpty
    @Size(max = 50)
    private String name;

    @NotEmpty
    @Digits(integer = 12, fraction = 3)
    private BigDecimal price;

    @NotEmpty
    private ProductType productType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }
}
