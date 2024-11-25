package com.local.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Cart {
    private int id;
    private User user;
    private Set<Product> products;
    private LocalDateTime lastUpdateTime;
    private LocalDateTime processTime;

    public Cart(int id, User user, Set<Product> products, LocalDateTime lastUpdateTime, LocalDateTime processTime) {
        this.id = id;
        this.user = user;
        this.products = products;
        this.lastUpdateTime = lastUpdateTime;
        this.processTime = processTime;
    }

    public Cart(Cart cart){
        this(cart.getId(), null, null, cart.getLastUpdateTime(), cart.getProcessTime());
        User user = new User(cart.getUser());
        this.setUser(user);
        Set<Product> products = new HashSet<>();
        for (Product product : cart.getProducts()) {
            products.add(new Product(product));
        }
        this.setProducts(products);
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

    public LocalDateTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    public LocalDateTime getProcessTime() {
        return processTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public void setProcessTime(LocalDateTime processTime) {
        this.processTime = processTime;
    }
}
