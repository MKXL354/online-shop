package com.local.servlet.productmanagement;

import com.local.model.Product;
import com.local.servlet.JsonFormatException;
import com.local.servlet.ObjectValidationFilter;
import jakarta.servlet.*;

import java.io.IOException;

public class ProductObjectValidationFilter extends ObjectValidationFilter {
    @Override
    protected Object getObjectToValidate(ServletRequest servletRequest) throws IOException, JsonFormatException {
        return super.commonWebComponentService.getObjectFromJsonRequest(servletRequest, Product.class);
    }

    @Override
    protected void setObjectAsAttribute(ServletRequest servletRequest, Object obj) {
        servletRequest.setAttribute("product", obj);
    }
}
