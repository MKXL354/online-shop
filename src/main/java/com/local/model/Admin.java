package com.local.model;

import java.util.UUID;

public class Admin extends User{

    public Admin(UUID id, String username, String password) {
        super(id, username, password);
    }
}
