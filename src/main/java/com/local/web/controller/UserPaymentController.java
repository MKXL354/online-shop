package com.local.web.controller;

import com.local.persistence.DAOException;
import com.local.exception.service.payment.PendingPaymentNotFoundException;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.model.Payment;
import com.local.model.UserType;
import com.local.service.CommonService;
import com.local.service.UserService;
import com.local.web.auth.AuthRequired;
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

    @AuthRequired(UserType.WEB_USER)
    @GetMapping
    public ResponseEntity<Payment> getPendingPayment(@RequestAttribute int userId) throws UserNotFoundException, DAOException {
        return ResponseEntity.ok(commonService.getPendingPayment(userId));
    }

    @AuthRequired(UserType.WEB_USER)
    @DeleteMapping
    public ResponseEntity<Void> cancelPurchase(@RequestAttribute int userId) throws UserNotFoundException, DAOException, PendingPaymentNotFoundException {
        userService.cancelPurchase(userId);
        return ResponseEntity.ok().build();
    }
}
