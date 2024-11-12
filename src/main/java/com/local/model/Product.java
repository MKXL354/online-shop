package com.local.model;

import com.local.util.objectvalidator.NotNull;

import java.math.BigDecimal;
import java.util.Objects;

public class Product implements Comparable<Product>{
    private int id;

    @NotNull(message = "name can't be null")
    private String name;

    private BigDecimal price;

    private int count;

    public Product(int id, String name, BigDecimal price, int count) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
    }

    public Product(Product product) {
        this(product.getId(), product.getName(), product.getPrice(), product.getCount());
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
