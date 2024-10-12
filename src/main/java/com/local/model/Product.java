package com.local.model;

import com.local.validator.NotNull;

public class Product {
    @NotNull(message = "id can't be null")
    private int id;

    @NotNull(message = "name can't be null")
    private String name;

    @NotNull(message = "price can't be null")
    private float price;

    @NotNull(message = "count can't be null")
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
}
