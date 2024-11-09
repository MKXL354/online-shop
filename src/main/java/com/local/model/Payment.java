package com.local.model;

public class Payment {
    private int id;
    private User user;
    private Cart cart;
    private double amount;

    public Payment(int id, User user, Cart cart, double amount) {
        this.id = id;
        this.user = user;
        this.cart = cart;
        this.amount = amount;
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
}
