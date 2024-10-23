package com.local.servlet.userservice;

import com.local.dto.ProductDTO;
import com.local.servlet.ObjectValidationFilter;
import jakarta.servlet.*;

public class ProductDTOObjectValidationFilter extends ObjectValidationFilter {
    @Override
    protected Class<?> getObjectClass(ServletRequest servletRequest) {
        return ProductDTO.class;
    }

    @Override
    protected String getObjectName() {
        return "productDTO";
    }
}
