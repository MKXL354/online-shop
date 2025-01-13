package com.local.dto;

import com.local.entity.CartStatus;

public class CartDto {
    private long userId;
    private String lastUpdateTime;
    private CartStatus cartStatus;

    public CartDto(long userId, String lastUpdateTime, CartStatus cartStatus) {
        this.userId = userId;
        this.lastUpdateTime = lastUpdateTime;
        this.cartStatus = cartStatus;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public CartStatus getCartStatus() {
        return cartStatus;
    }

    public void setCartStatus(CartStatus cartStatus) {
        this.cartStatus = cartStatus;
    }
}
