package com.local.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Payment extends AbstractEntity{
    @ManyToOne(optional = false)
    private AppUser appUser;

    @ManyToOne(optional = false)
    private Cart cart;

    @Column(nullable = false, precision = 15, scale = 3)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime lastUpdateTime;

    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    public Payment() {}

    public Payment(AppUser appUser, Cart cart, BigDecimal amount, LocalDateTime lastUpdateTime, PaymentStatus paymentStatus) {
        this.appUser = appUser;
        this.cart = cart;
        this.amount = amount;
        this.lastUpdateTime = lastUpdateTime;
        this.paymentStatus = paymentStatus;
    }

    public AppUser getUser() {
        return appUser;
    }

    public void setUser(AppUser appUser) {
        this.appUser = appUser;
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
