package com.local.servlet.usermanagement;

import com.local.model.User;
import com.local.servlet.ObjectValidationFilter;
import jakarta.servlet.*;

public class UserObjectValidationFilter extends ObjectValidationFilter {
    @Override
    protected Class<?> getObjectClass(ServletRequest servletRequest) {
        return User.class;
    }

    @Override
    protected String getObjectName() {
        return "user";
    }
}
