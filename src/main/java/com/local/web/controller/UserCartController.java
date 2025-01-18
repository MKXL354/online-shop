package com.local.web.controller;

import com.local.dto.CartDto;
import com.local.entity.UserType;
import com.local.exception.service.productmanagement.ProductNotFoundException;
import com.local.exception.service.user.EmptyCartException;
import com.local.exception.service.user.PreviousPaymentPendingException;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.service.UserService;
import com.local.web.auth.AuthRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/cart")
public class UserCartController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @AuthRequired(UserType.WEB_USER)
    @PostMapping("/products/{name}")
    public ResponseEntity<Void> addProductToCart(@PathVariable String name, @RequestAttribute int userId) throws UserNotFoundException, PreviousPaymentPendingException, ProductNotFoundException {
        userService.addProductToCart(userId, name);
        return ResponseEntity.ok().build();
    }

    @AuthRequired(UserType.WEB_USER)
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> removeProductFromCart(@PathVariable int id, @RequestAttribute int userId) throws UserNotFoundException, ProductNotFoundException {
        userService.removeProductFromCart(userId, id);
        return ResponseEntity.ok().build();
    }

    @AuthRequired(UserType.WEB_USER)
    @GetMapping
    public ResponseEntity<CartDto> getActiveCart(@RequestAttribute int userId) throws UserNotFoundException {
        return ResponseEntity.ok(userService.getActiveCartDto(userId));
    }

    @AuthRequired(UserType.WEB_USER)
    @PostMapping("/finalize")
    public ResponseEntity<Void> finalizePurchase(@RequestAttribute int userId) throws UserNotFoundException, EmptyCartException {
        userService.finalizePurchase(userId);
        return ResponseEntity.ok().build();
    }
}
//TODO: add/remove with unified api? both with name
