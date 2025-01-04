package com.local.web.controller;

import com.local.dao.DAOException;
import com.local.model.UserType;
import com.local.web.auth.AuthRequired;
import com.local.web.dto.LoginCredentialsDTO;
import com.local.exception.service.usermanagement.DuplicateUsernameException;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.exception.service.usermanagement.WrongPasswordException;
import com.local.model.User;
import com.local.service.UserManagementService;
import com.local.util.token.TokenManager;
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
    public ResponseEntity<User> addUser(@RequestBody User user) throws DAOException, DuplicateUsernameException {
        return ResponseEntity.ok(userManagementService.addUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginCredentialsDTO loginCredentials) throws UserNotFoundException, DAOException, WrongPasswordException {
        String username = loginCredentials.getUsername();
        String password = loginCredentials.getPassword();
        User user = userManagementService.login(username, password);

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getType());
        claims.put("userId", user.getId());
        String jws = tokenManager.getSignedToken(claims);

        return ResponseEntity.ok().header("Authorization", jws).body(user);
    }
}
