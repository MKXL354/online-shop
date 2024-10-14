package com.local.servlet.usermanagement;

import com.local.model.User;
import com.local.servlet.JsonFormatException;
import com.local.servlet.ObjectValidationFilter;
import jakarta.servlet.*;

import java.io.IOException;

public class UserObjectValidationFilter extends ObjectValidationFilter {
    @Override
    protected Object getObjectToValidate(ServletRequest servletRequest) throws IOException, JsonFormatException {
        return super.commonWebComponentService.getObjectFromJsonRequest(servletRequest, User.class);
    }

    @Override
    protected void setObjectAsAttribute(ServletRequest servletRequest, Object obj) {
        servletRequest.setAttribute("user", obj);
    }
}
