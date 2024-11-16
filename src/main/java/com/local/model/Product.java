package com.local.model;

import com.local.util.objectvalidator.NotNull;

import java.math.BigDecimal;
import java.util.Objects;

public class Product implements Comparable<Product>{
    protected int id;

    @NotNull(message = "name can't be null")
    protected String name;

    protected BigDecimal price;

    protected int count;

    @NotNull(message = "wrong or null product type")
    protected ProductType type;

    public Product(int id, String name, BigDecimal price, int count, ProductType type) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.type = type;
    }

    public Product(Product product) {
        this(product.getId(), product.getName(), product.getPrice(), product.getCount(), product.getType());
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

    public int getCount() {
        return count;
    }

    public ProductType getType() {
        return type;
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

    public void setCount(int count) {
        this.count = count;
    }

    public void setType(ProductType type) {
        this.type = type;
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
}
