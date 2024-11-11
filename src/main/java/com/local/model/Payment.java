package com.local.model;

import java.time.LocalDateTime;

public class Payment {
    private int id;
    private User user;
    private Cart cart;
    private double amount;
    private LocalDateTime lastUpdate;
    private PaymentStatus status;

    public Payment(int id, User user, Cart cart, double amount, LocalDateTime lastUpdate, PaymentStatus status) {
        this.id = id;
        this.user = user;
        this.cart = cart;
        this.amount = amount;
        this.lastUpdate = lastUpdate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}
