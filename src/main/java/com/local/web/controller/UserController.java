package com.local.web.controller;

import com.local.dto.AppUserDto;
import com.local.entity.AppUser;
import com.local.entity.UserType;
import com.local.exception.service.usermanagement.DuplicateUsernameException;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.exception.service.usermanagement.WrongUserPasswordException;
import com.local.service.UserManagementService;
import com.local.util.token.TokenManager;
import com.local.web.auth.AuthRequired;
import jakarta.validation.Valid;
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
    public ResponseEntity<AppUserDto> addUser(@Valid @RequestBody AppUserDto appUserDto) throws DuplicateUsernameException {
        return ResponseEntity.ok(userManagementService.addUser(appUserDto));
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody AppUserDto appUserDto) throws UserNotFoundException, WrongUserPasswordException {
        AppUser appUser = userManagementService.login(appUserDto);

        //TODO: use AuthUtil here as well
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", appUser.getUserType());
        claims.put("userId", appUser.getId());
        String jws = tokenManager.getToken(claims);

        return ResponseEntity.ok().header("Authorization", jws).build();
    }
}
