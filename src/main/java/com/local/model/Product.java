package com.local.model;

import java.util.UUID;

public class Product {
    private final UUID id;
    private String name;
    private float price;
    private int count;

    public Product(UUID id, String name, float price, int count) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
    }

    public UUID getId() {
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

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
