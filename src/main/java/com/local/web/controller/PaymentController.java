package com.local.web.controller;

import com.local.exception.service.payment.*;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.model.Card;
import com.local.model.UserType;
import com.local.persistence.DAOException;
import com.local.service.PaymentService;
import com.local.web.auth.AuthRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController()
public class PaymentController {
    private PaymentService paymentService;

    @Autowired
    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @AuthRequired(UserType.ADMIN)
    @PostMapping("/card")
    public ResponseEntity<Card> addCard(@RequestBody Card card) throws DAOException, DuplicateCardException {
        return ResponseEntity.ok(paymentService.addCard(card));
    }

    @AuthRequired(UserType.ADMIN)
    @PostMapping("card/{id}/balance/{amount}")
    public ResponseEntity<Void> addBalance(@PathVariable int id, @PathVariable BigDecimal amount) throws CardNotFoundException, DAOException {
        paymentService.addBalance(id, amount);
        return ResponseEntity.ok().build();
    }

    @AuthRequired(UserType.WEB_USER)
    @PostMapping("user/pay")
    public ResponseEntity<Void> cardPay(@RequestAttribute int userId, @RequestBody Card card) throws UserNotFoundException, PendingPaymentNotFoundException, InvalidCardException, InsufficientBalanceException, PaymentInProgressException, DAOException {
        paymentService.cardPay(userId, card);
        return ResponseEntity.ok().build();
    }
}
