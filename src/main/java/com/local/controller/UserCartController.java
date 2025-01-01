package com.local.controller;

import com.local.dao.DAOException;
import com.local.exception.service.productmanagement.ProductNotFoundException;
import com.local.exception.service.user.EmptyCartException;
import com.local.exception.service.user.PreviousPaymentPendingException;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.model.Cart;
import com.local.service.CommonService;
import com.local.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/cart")
public class UserCartController {
    private UserService userService;
    private CommonService commonService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setCommonService(CommonService commonService) {
        this.commonService = commonService;
    }

    @PostMapping("/products/{name}")
    public void addProductToCart(@PathVariable String name, HttpServletRequest request) throws UserNotFoundException, DAOException, PreviousPaymentPendingException, ProductNotFoundException {
        int userId = (int) request.getAttribute("userId");
        userService.addProductToCart(userId, name);
    }

    @DeleteMapping("/products/{id}")
    public void removeProductFromCart(@PathVariable int id, HttpServletRequest request) throws UserNotFoundException, DAOException, ProductNotFoundException {
        int userId = (int) request.getAttribute("userId");
        userService.removeProductFromCart(userId, id);
    }

    @GetMapping
    public Cart getActiveCart(HttpServletRequest request) throws UserNotFoundException, DAOException {
        int userId = (int) request.getAttribute("userId");
        return commonService.getActiveCart(userId);
    }

    @PostMapping("/finalize")
    public void finalizePurchase(HttpServletRequest request) throws UserNotFoundException, DAOException, EmptyCartException {
        int userId = (int) request.getAttribute("userId");
        userService.finalizePurchase(userId);
    }


}
//TODO: add/remove with unified api? both with name
