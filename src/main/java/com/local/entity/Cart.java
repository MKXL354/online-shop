package com.local.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Cart extends AbstractEntity {
    @ManyToOne(optional = false)
    private AppUser appUser;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Map<Long, Product> products = new HashMap<>();

    @Column(nullable = false)
    private LocalDateTime lastUpdateTime;

    @Column(nullable = false)
    private CartStatus cartStatus;

    public Cart() {}

    public Cart(AppUser appUser) {
        this.appUser = appUser;
        this.lastUpdateTime = LocalDateTime.now();
        this.cartStatus = CartStatus.ACTIVE;
    }

    public AppUser getUser() {
        return appUser;
    }

    public void setUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Map<Long, Product> getProducts() {
        return products;
    }

    public void setProducts(Map<Long, Product> products) {
        this.products = products;
    }

    public LocalDateTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public CartStatus getCartStatus() {
        return cartStatus;
    }

    public void setCartStatus(CartStatus cartStatus) {
        this.cartStatus = cartStatus;
    }
}
