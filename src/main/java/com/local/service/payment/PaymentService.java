package com.local.service.payment;

import com.local.model.User;

public interface PaymentService {
    void pay(User user, double amount);
}
//TODO: throw service exception in case of abstraction? not good -> generic errors
