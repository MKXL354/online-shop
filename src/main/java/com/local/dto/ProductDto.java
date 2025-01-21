package com.local.dto;

import com.local.entity.Product;
import com.local.entity.ProductStatus;
import com.local.entity.ProductType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class ProductDto extends AbstractDto{
    @NotBlank
    @Size(max = 50)
    private String name;

    @NotNull
    @Digits(integer = 12, fraction = 3)
    @Positive
    private BigDecimal price;

    @NotNull
    private ProductStatus productStatus;

    @NotNull
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
