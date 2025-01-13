package com.local.service;

import com.local.dto.UserDto;
import com.local.entity.User;
import com.local.exception.service.usermanagement.DuplicateUsernameException;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.exception.service.usermanagement.WrongPasswordException;
import com.local.repository.UserRepo;
import com.local.util.password.PasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserManagementService {
    private UserRepo userRepo;
    private PasswordEncryptor passwordEncryptor;

    @Autowired
    public void setUserRepo(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Autowired
    public void setPasswordEncryptor(PasswordEncryptor passwordEncryptor) {
        this.passwordEncryptor = passwordEncryptor;
    }
    
    public User addUser(User user) throws DuplicateUsernameException {
        if (userRepo.getUserByUsername(user.getUsername()) != null) {
            throw new DuplicateUsernameException("duplicate username not allowed", null);
        }
        String hashedPassword = passwordEncryptor.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        return userRepo.addUser(user);
    }

    public User login(UserDto userDto) throws UserNotFoundException, WrongPasswordException {
        User user = userRepo.findByUsername(userDto.getUsername()).orElseThrow(() -> new UserNotFoundException("user not found", null));
        if (!passwordEncryptor.checkPassword(user.getPassword(), user.getPassword())) {
            throw new WrongPasswordException("wrong password", null);
        }
        return user;
    }
}
