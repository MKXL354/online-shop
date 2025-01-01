package com.local.controller;

import com.local.dao.DAOException;
import com.local.exception.service.payment.PendingPaymentNotFoundException;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.model.Payment;
import com.local.service.CommonService;
import com.local.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Payment getPendingPayment(HttpServletRequest request) throws UserNotFoundException, DAOException {
        int userId = (int) request.getAttribute("userId");
        return commonService.getPendingPayment(userId);
    }

    @DeleteMapping
    public void cancelPurchase(HttpServletRequest request) throws UserNotFoundException, DAOException, PendingPaymentNotFoundException {
        int userId = (int) request.getAttribute("userId");
        userService.cancelPurchase(userId);
    }
}
