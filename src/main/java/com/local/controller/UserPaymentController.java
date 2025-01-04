package com.local.controller;

import com.local.dao.DAOException;
import com.local.exception.service.payment.PendingPaymentNotFoundException;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.model.Payment;
import com.local.service.CommonService;
import com.local.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/payment")
public class UserPaymentController {
    private CommonService commonService;
    private UserService userService;

    @Autowired
    public void setPaymentService(CommonService commonService) {
        this.commonService = commonService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Payment> getPendingPayment(@RequestAttribute int userId) throws UserNotFoundException, DAOException {
        return ResponseEntity.ok(commonService.getPendingPayment(userId));
    }

    @DeleteMapping
    public ResponseEntity<Void> cancelPurchase(@RequestAttribute int userId) throws UserNotFoundException, DAOException, PendingPaymentNotFoundException {
        userService.cancelPurchase(userId);
        return ResponseEntity.noContent().build();
    }
}
