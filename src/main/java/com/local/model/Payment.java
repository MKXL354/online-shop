package com.local.model;

import java.math.BigDecimal;

public class Payment implements Comparable<Payment>{
    private int id;
    private User user;
    private Cart cart;
    private BigDecimal amount;
    private String lastUpdate;
    private PaymentStatus status;

    public Payment(int id, User user, Cart cart, BigDecimal amount, String lastUpdate, PaymentStatus status) {
        this.id = id;
        this.user = user;
        this.cart = cart;
        this.amount = amount;
        this.lastUpdate = lastUpdate;
        this.status = status;
    }

    public Payment(Payment payment){
        this(payment.getId(), null, null, payment.getAmount(), payment.getLastUpdate(), payment.getStatus());
        User newUser = new User(payment.getUser());
        Cart cart = new Cart(payment.getCart());
        this.setUser(newUser);
        this.setCart(cart);
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    @Override
    public int compareTo(Payment payment) {
        return Integer.compare(this.id, payment.id);
    }
}
