package com.local.model;

import java.time.LocalDateTime;
import java.util.Set;

public class Cart {
    private int id;
    private User user;
    private Set<Product> products;
    private LocalDateTime processTime;

    public Cart(int id, User user, Set<Product> products, LocalDateTime processTime) {
        this.id = id;
        this.user = user;
        this.products = products;
        this.processTime = processTime;
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

    public LocalDateTime getProcessTime() {
        return processTime;
    }

    public void setProcessTime(){
        this.processTime = LocalDateTime.now();
    }
}
