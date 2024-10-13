package com.local.servlet.productmanagement;

import com.local.model.Product;
import com.local.servlet.CommonWebComponentService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AddUpdateProductFilter implements Filter {
    private CommonWebComponentService commonWebComponentService;

    @Override
    public void init(FilterConfig filterConfig) {
        commonWebComponentService = (CommonWebComponentService)filterConfig.getServletContext().getAttribute("commonWebComponentService");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Product product = commonWebComponentService.getObjectFromJsonRequest(servletRequest, Product.class);
        String violationMessages = commonWebComponentService.validateObject(product);
        if(violationMessages.isEmpty()){
            servletRequest.setAttribute("product", product);
            filterChain.doFilter(servletRequest, servletResponse);
        }
        else{
            HttpServletResponse httpResponse = (HttpServletResponse)servletResponse;
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            commonWebComponentService.writeResponse(httpResponse, violationMessages);
        }
    }
}
