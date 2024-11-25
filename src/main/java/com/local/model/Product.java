package com.local.model;

import com.local.util.objectvalidator.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class Product implements Comparable<Product>, Serializable {
    @Serial
    private static final long serialVersionUID = -4599586234479562592L;

    protected int id;

    @NotNull(message = "name can't be null")
    protected String name;

    @NotNull(message = "price can't be null")
    protected BigDecimal price;

    @NotNull(message = "wrong or null product type")
    protected ProductType type;

    protected ProductStatus status;

    public Product(int id, String name, BigDecimal price, ProductType type, ProductStatus status) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.type = type;
        this.status = status;
    }

    public Product(Product product) {
        this(product.getId(), product.getName(), product.getPrice(), product.getType(), product.getStatus());
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

    public ProductType getType() {
        return type;
    }

    public ProductStatus getStatus() {
        return status;
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

    public void setType(ProductType type) {
        this.type = type;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Product product) {
        return Integer.compare(this.id, product.id);
    }
//    TODO: equals, hashCode & compareTo might not be needed here
}
