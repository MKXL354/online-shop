package com.local.model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment implements Serializable {
    @Serial
    private static final long serialVersionUID = -452546719247246259L;

    private int id;
    private User user;
    private Cart cart;
    private BigDecimal amount;
    private LocalDateTime lastUpdateTime;
    private PaymentStatus paymentStatus;

    public Payment(int id, User user, Cart cart, BigDecimal amount, LocalDateTime lastUpdateTime, PaymentStatus paymentStatus) {
        this.id = id;
        this.user = user;
        this.cart = cart;
        this.amount = amount;
        this.lastUpdateTime = lastUpdateTime;
        this.paymentStatus = paymentStatus;
    }

    public Payment(Payment payment){
        this(payment.getId(), null, null, payment.getAmount(), payment.getLastUpdateTime(), payment.getPaymentStatus());
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

    public LocalDateTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
