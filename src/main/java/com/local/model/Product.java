package com.local.model;

import com.local.util.objectvalidator.NotNull;

import java.util.Objects;

public class Product {
    private final int id;

    @NotNull(message = "name can't be null")
    private String name;

    private double price;

    private int count;

    public Product(int id, String name, double price, int count) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getCount() {
        return count;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return id == product.id && Double.compare(price, product.price) == 0 && count == product.count && Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, count);
    }
}
