package com.local.service;

import com.local.dto.AppUserDto;
import com.local.entity.AppUser;
import com.local.exception.service.usermanagement.DuplicateUsernameException;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.exception.service.usermanagement.WrongUserPasswordException;
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
    
    public AppUserDto addUser(AppUserDto appUserDto) throws DuplicateUsernameException {
        if (userRepo.findByUsername(appUserDto.getUsername()).isPresent()) {
            throw new DuplicateUsernameException("duplicate username not allowed", null);
        }
        String hashedPassword = passwordEncryptor.hashPassword(appUserDto.getPassword());
        AppUser appUser = new AppUser(appUserDto.getUsername(), hashedPassword, appUserDto.getUserType());
        userRepo.save(appUser);
        return new AppUserDto(appUser);
    }

    public AppUser login(AppUserDto appUserDto) throws UserNotFoundException, WrongUserPasswordException {
        AppUser actualUser = userRepo.findByUsername(appUserDto.getUsername()).orElseThrow(() -> new UserNotFoundException("appUser not found", null));
        if (!passwordEncryptor.checkPassword(appUserDto.getPassword(), actualUser.getPassword())) {
            throw new WrongUserPasswordException("wrong user password", null);
        }
        return actualUser;
    }
}
