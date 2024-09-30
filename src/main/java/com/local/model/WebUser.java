package com.local.model;

import java.util.UUID;

public class WebUser extends User{
    private Cart cart;

    public WebUser(UUID id, String username, String password, Cart cart) {
        super(id, username, password);
        this.cart = cart;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
