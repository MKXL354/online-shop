package com.local.servlet;

import com.local.model.Product;
import jakarta.servlet.*;

import java.io.IOException;

public class ProductObjectValidationFilter extends ObjectValidationFilter {
    private Product product;

    @Override
    protected Object getObjectToValidate(ServletRequest servletRequest) throws IOException {
        product = super.commonWebComponentService.getObjectFromJsonRequest(servletRequest, Product.class);
        return product;
    }

    @Override
    protected void setObjectAsAttribute(ServletRequest servletRequest) {
        servletRequest.setAttribute("product", product);
    }
}
