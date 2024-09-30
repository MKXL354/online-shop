package com.local.model;

import java.util.Set;
import java.util.UUID;

public class Cart {
    private final UUID id;
    private final User owner;
    private Set<Product> products;

    public Cart(UUID id, User owner, Set<Product> products) {
        this.id = id;
        this.owner = owner;
        this.products = products;
    }

    public UUID getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
