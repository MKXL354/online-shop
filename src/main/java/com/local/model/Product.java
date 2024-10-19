package com.local.model;

import com.local.util.objectvalidator.NotNull;

import java.util.Objects;

public class Product {
    private int id;

    @NotNull(message = "name can't be null")
    private String name;

    private float price;

    private int count;

    public Product(int id, String name, float price, int count) {
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

    public float getPrice() {
        return price;
    }

    public int getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return id == product.id && Float.compare(price, product.price) == 0 && count == product.count && Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, count);
    }
}
