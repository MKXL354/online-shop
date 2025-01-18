package com.local.service;

import com.local.entity.AppUser;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.repository.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommonService {
    private UserRepo userRepo;

    public AppUser getUserById(long userId) throws UserNotFoundException {
        return userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("user not found", null));
    }
}
