package com.local.dto;

import com.local.entity.PaymentStatus;

import java.math.BigDecimal;

public class PaymentDto {
    private long userId;
    private long cartId;
    private BigDecimal amount;
    private String lastUpdateTime;
    private PaymentStatus paymentStatus;

    public PaymentDto(long userId, long cartId, BigDecimal amount, String lastUpdateTime, PaymentStatus paymentStatus) {
        this.userId = userId;
        this.cartId = cartId;
        this.amount = amount;
        this.lastUpdateTime = lastUpdateTime;
        this.paymentStatus = paymentStatus;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCartId() {
        return cartId;
    }

    public void setCartId(long cartId) {
        this.cartId = cartId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
