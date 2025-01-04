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

    @PostMapping("/products/{name}")
    public ResponseEntity<Void> addProductToCart(@PathVariable String name, @RequestAttribute int userId) throws UserNotFoundException, DAOException, PreviousPaymentPendingException, ProductNotFoundException {
        userService.addProductToCart(userId, name);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> removeProductFromCart(@PathVariable int id, @RequestAttribute int userId) throws UserNotFoundException, DAOException, ProductNotFoundException {
        userService.removeProductFromCart(userId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Cart> getActiveCart(@RequestAttribute int userId) throws UserNotFoundException, DAOException {
        return ResponseEntity.ok(commonService.getActiveCart(userId));
    }

    @PostMapping("/finalize")
    public ResponseEntity<Void> finalizePurchase(@RequestAttribute int userId) throws UserNotFoundException, DAOException, EmptyCartException {
        userService.finalizePurchase(userId);
        return ResponseEntity.noContent().build();
    }
}
//TODO: add/remove with unified api? both with name
