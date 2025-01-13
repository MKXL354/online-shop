package com.local.web.controller;

import com.local.dto.PaymentDto;
import com.local.entity.Payment;
import com.local.entity.UserType;
import com.local.exception.service.payment.PendingPaymentNotFoundException;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.service.PaymentService;
import com.local.service.UserService;
import com.local.web.auth.AuthRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/payment")
public class UserPaymentController {
    private PaymentService paymentService;
    private UserService userService;

    @Autowired
    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @AuthRequired(UserType.WEB_USER)
    @GetMapping
    public ResponseEntity<PaymentDto> getPendingPayment(@RequestAttribute long userId) throws PendingPaymentNotFoundException {
        Payment payment = paymentService.getPendingPayment(userId);
//        FIXME: potential lazy fetch bug
        return ResponseEntity.ok(new PaymentDto(payment.getUser().getId(), payment.getCart().getId(), payment.getAmount(), payment.getLastUpdateTime().toString(), payment.getPaymentStatus()));
    }

    @AuthRequired(UserType.WEB_USER)
    @DeleteMapping
    public ResponseEntity<Void> cancelPurchase(@RequestAttribute long userId) throws UserNotFoundException, PendingPaymentNotFoundException {
        userService.cancelPurchase(userId);
        return ResponseEntity.ok().build();
    }
}
