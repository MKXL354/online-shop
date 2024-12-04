package com.local.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Cart implements Serializable {
    @Serial
    private static final long serialVersionUID = -4617074480075035012L;

    private int id;
    private User user;
    private Map<Integer, Product> products;
    private LocalDateTime lastUpdateTime;
    private CartStatus cartStatus;

    public Cart(int id, User user, Map<Integer, Product> products, LocalDateTime lastUpdateTime, CartStatus cartStatus) {
        this.id = id;
        this.user = user;
        this.products = products;
        this.lastUpdateTime = lastUpdateTime;
        this.cartStatus = cartStatus;
    }

    public Cart(Cart cart){
        this(cart.getId(), null, null, cart.getLastUpdateTime(), cart.getCartStatus());
        User user = new User(cart.getUser());
        this.setUser(user);
        Map<Integer, Product> products = new HashMap<>();
        for (Product product : cart.getProducts().values()) {
            products.put(product.getId(), new Product(product));
        }
        this.setProducts(products);
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Map<Integer, Product> getProducts() {
        return products;
    }

    public LocalDateTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    public CartStatus getCartStatus() {
        return cartStatus;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setProducts(Map<Integer, Product> products) {
        this.products = products;
    }

    public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public void setCartStatus(CartStatus cartStatus) {
        this.cartStatus = cartStatus;
    }
}
