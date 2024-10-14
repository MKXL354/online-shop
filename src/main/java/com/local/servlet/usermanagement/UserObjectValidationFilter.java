package com.local.servlet.usermanagement;

import com.local.model.User;
import com.local.servlet.JsonFormatException;
import com.local.servlet.ObjectValidationFilter;
import jakarta.servlet.*;

import java.io.IOException;

public class UserObjectValidationFilter extends ObjectValidationFilter {
    private User user;

    @Override
    protected Object getObjectToValidate(ServletRequest servletRequest) throws IOException, JsonFormatException {
        user = super.commonWebComponentService.getObjectFromJsonRequest(servletRequest, User.class);
        return user;
    }

    @Override
    protected void setObjectAsAttribute(ServletRequest servletRequest) {
        servletRequest.setAttribute("user", user);
    }
}
