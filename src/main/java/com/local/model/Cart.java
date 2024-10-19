package com.local.model;

import java.util.Set;

public class Cart {
    private int id;
    private User user;
    private Set<Product> products;

    public Cart(int id, User user, Set<Product> products) {
        this.id = id;
        this.user = user;
        this.products = products;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Set<Product> getProducts() {
        return products;
    }
}
