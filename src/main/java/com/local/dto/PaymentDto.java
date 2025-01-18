package com.local.dto;

import com.local.entity.Payment;
import com.local.entity.PaymentStatus;
import com.local.util.validation.ValidLocalDateTime;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class PaymentDto extends AbstractDto{
    @PositiveOrZero
    private long userId;

    @PositiveOrZero
    private long cartId;

    @NotNull
    @Positive
    private BigDecimal amount;

    @ValidLocalDateTime
    private String lastUpdateTime;

    @NotNull
    private PaymentStatus paymentStatus;

    public PaymentDto(Payment payment) {
        this.id = payment.getId();
        this.userId = payment.getUser().getId();
        this.cartId = payment.getCart().getId();
        this.amount = payment.getAmount();
        this.lastUpdateTime = payment.getLastUpdateTime().toString();
        this.paymentStatus = payment.getPaymentStatus();
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
