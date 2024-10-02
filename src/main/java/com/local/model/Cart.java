package com.local.model;

import java.util.Set;

public class Cart {
    private int id;
    private User owner;
    private Set<Product> products;

    public Cart(int id, User owner, Set<Product> products) {
        this.id = id;
        this.owner = owner;
        this.products = products;
    }

    public int getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public Set<Product> getProducts() {
        return products;
    }
}
