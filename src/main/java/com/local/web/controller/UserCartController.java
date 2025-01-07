package com.local.web.controller;

import com.local.persistence.DAOException;
import com.local.exception.service.productmanagement.ProductNotFoundException;
import com.local.exception.service.user.EmptyCartException;
import com.local.exception.service.user.PreviousPaymentPendingException;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.model.Cart;
import com.local.model.UserType;
import com.local.service.CommonService;
import com.local.service.UserService;
import com.local.web.auth.AuthRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @AuthRequired(UserType.WEB_USER)
    @PostMapping("/products/{name}")
    public ResponseEntity<Void> addProductToCart(@PathVariable String name, @RequestAttribute int userId) throws UserNotFoundException, DAOException, PreviousPaymentPendingException, ProductNotFoundException {
        userService.addProductToCart(userId, name);
        return ResponseEntity.ok().build();
    }

    @AuthRequired(UserType.WEB_USER)
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> removeProductFromCart(@PathVariable int id, @RequestAttribute int userId) throws UserNotFoundException, DAOException, ProductNotFoundException {
        userService.removeProductFromCart(userId, id);
        return ResponseEntity.ok().build();
    }

    @AuthRequired(UserType.WEB_USER)
    @GetMapping
    public ResponseEntity<Cart> getActiveCart(@RequestAttribute int userId) throws UserNotFoundException, DAOException {
        return ResponseEntity.ok(commonService.getActiveCart(userId));
    }

    @AuthRequired(UserType.WEB_USER)
    @PostMapping("/finalize")
    public ResponseEntity<Void> finalizePurchase(@RequestAttribute int userId) throws UserNotFoundException, DAOException, EmptyCartException {
        userService.finalizePurchase(userId);
        return ResponseEntity.ok().build();
    }
}
//TODO: add/remove with unified api? both with name
