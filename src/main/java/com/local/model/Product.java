package com.local.model;

import com.local.util.objectvalidator.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public class Product implements Serializable {
    @Serial
    private static final long serialVersionUID = -4599586234479562592L;

    protected int id;

    @NotNull(message = "name can't be null")
    protected String name;

    @NotNull(message = "price can't be null")
    protected BigDecimal price;

    @NotNull(message = "wrong or null product type")
    protected ProductType productType;

    protected ProductStatus productStatus;

    public Product(int id, String name, BigDecimal price, ProductType productType, ProductStatus productStatus) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.productType = productType;
        this.productStatus = productStatus;
    }

    public Product(Product product) {
        this(product.getId(), product.getName(), product.getPrice(), product.getProductType(), product.getProductStatus());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductType getProductType() {
        return productType;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setType(ProductType productType) {
        this.productType = productType;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }
}
