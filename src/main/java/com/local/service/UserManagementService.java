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
    
    public User addUser(UserDto userDto) throws DuplicateUsernameException {
        if (userRepo.findByUsername(userDto.getUsername()).isPresent()) {
            throw new DuplicateUsernameException("duplicate username not allowed", null);
        }
        return userRepo.save(new User(userDto.getUsername(), userDto.getPassword(), userDto.getUserType()));
    }

    public User login(UserDto userDto) throws UserNotFoundException, WrongPasswordException {
        User user = userRepo.findByUsername(userDto.getUsername()).orElseThrow(() -> new UserNotFoundException("user not found", null));
        if (!passwordEncryptor.checkPassword(user.getPassword(), user.getPassword())) {
            throw new WrongPasswordException("wrong password", null);
        }
        return user;
    }
}
