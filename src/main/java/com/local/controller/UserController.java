package com.local.controller;

import com.local.dao.DAOException;
import com.local.dto.LoginCredentialsDTO;
import com.local.exception.service.usermanagement.DuplicateUsernameException;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.exception.service.usermanagement.WrongPasswordException;
import com.local.model.User;
import com.local.service.UserManagementService;
import com.local.util.token.TokenManager;
import jakarta.servlet.http.HttpServletResponse;
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

    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody User user) throws DAOException, DuplicateUsernameException {
        return ResponseEntity.ok(userManagementService.addUser(user));
    }

    @PostMapping("/login")
    public User login(@RequestBody LoginCredentialsDTO loginCredentials, HttpServletResponse response) throws UserNotFoundException, DAOException, WrongPasswordException {
        String username = loginCredentials.getUsername();
        String password = loginCredentials.getPassword();
        User user = userManagementService.login(username, password);

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getType().toString());
        claims.put("userId", user.getId());
        String jws = tokenManager.getSignedToken(claims);

        response.setHeader("Authorization", jws);
        return user;
    }
}
