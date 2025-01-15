package com.local.dto;

import com.local.entity.Product;
import com.local.entity.ProductStatus;
import com.local.entity.ProductType;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class ProductDto extends AbstractDto{
    @NotEmpty
    @Size(max = 50)
    private String name;

    @NotEmpty
    @Digits(integer = 12, fraction = 3)
    @Positive
    private BigDecimal price;

    @NotEmpty
    private ProductStatus productStatus;

    @NotEmpty
    private ProductType productType;

    public ProductDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.productStatus = product.getProductStatus();
        this.productType = product.getProductType();
    }

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

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }
}
