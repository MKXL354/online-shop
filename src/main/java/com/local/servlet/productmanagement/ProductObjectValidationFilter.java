package com.local.servlet.productmanagement;

import com.local.model.Product;
import com.local.servlet.ObjectValidationFilter;
import jakarta.servlet.*;

public class ProductObjectValidationFilter extends ObjectValidationFilter {
    @Override
    protected Class<?> getObjectClass(ServletRequest servletRequest) {
        return Product.class;
    }

    @Override
    protected String getObjectName() {
        return "product";
    }
}
