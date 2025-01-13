package com.local.web.controller;

import com.local.dto.UserDto;
import com.local.entity.User;
import com.local.entity.UserType;
import com.local.exception.service.usermanagement.DuplicateUsernameException;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.exception.service.usermanagement.WrongPasswordException;
import com.local.service.UserManagementService;
import com.local.util.token.TokenManager;
import com.local.web.auth.AuthRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    private UserManagementService userManagementService;
    private TokenManager tokenManager;

    @Autowired
    public void setUserManagementService(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @Autowired
    public void setTokenManager(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @AuthRequired(UserType.ADMIN)
    @PostMapping("/users")
    public ResponseEntity<Long> addUser(@RequestBody UserDto userDto) throws DuplicateUsernameException {
        return ResponseEntity.ok(userManagementService.addUser(userDto).getId());
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody UserDto userDto) throws UserNotFoundException, WrongPasswordException {
        User user = userManagementService.login(userDto);

        //TODO: use AuthUtil here as well
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getUserType());
        claims.put("userId", user.getId());
        String jws = tokenManager.getToken(claims);

        return ResponseEntity.ok().header("Authorization", jws).build();
    }
}
