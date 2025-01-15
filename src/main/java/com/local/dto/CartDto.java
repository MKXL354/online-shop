package com.local.dto;

import com.local.entity.Cart;
import com.local.entity.CartStatus;
import com.local.entity.Product;
import com.local.util.validation.ValidLocalDateTime;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.HashSet;
import java.util.Set;

public class CartDto extends AbstractDto{
    @NotEmpty
    @PositiveOrZero
    private long userId;

    @NotNull
    private Set<Long> productIds;

    @ValidLocalDateTime
    private String lastUpdateTime;

    @NotEmpty
    private CartStatus cartStatus;

    public CartDto(Cart cart) {
        this.id = cart.getId();
        this.userId = cart.getUser().getId();
        this.productIds = new HashSet<>();
        for (Product product : cart.getProducts()) {
            this.productIds.add(product.getId());
        }
        this.cartStatus = cart.getCartStatus();
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Set<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(Set<Long> productIds) {
        this.productIds = productIds;
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
