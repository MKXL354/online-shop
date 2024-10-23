package com.local.service.payment;

import com.local.model.User;

public interface PaymentService {
    void pay(User user, double amount);
}
//TODO: service exception?
