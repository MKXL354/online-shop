package com.local.servlet.productmanagement;

import com.local.commonexceptions.ApplicationRuntimeException;
import com.local.model.Product;
import com.local.servlet.CommonWebComponentService;
import com.local.util.objectvalidator.ObjectValidator;
import com.local.util.objectvalidator.ValidatorException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashSet;

public class AddUpdateProductFilter implements Filter {
    private CommonWebComponentService commonWebComponentService;
    private ObjectValidator validator;

    @Override
    public void init(FilterConfig filterConfig) {
        commonWebComponentService = (CommonWebComponentService)filterConfig.getServletContext().getAttribute("commonWebComponentService");
        validator = new ObjectValidator();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try{
            Product product = commonWebComponentService.getObjectFromJsonRequest(servletRequest, Product.class);
            HashSet<String> violationMessages = validator.validate(product);
            if(violationMessages.isEmpty()){
                servletRequest.setAttribute("product", product);
                filterChain.doFilter(servletRequest, servletResponse);
            }
            else{
                StringBuilder result = new StringBuilder();
                violationMessages.forEach(result::append);
                HttpServletResponse httpResponse = (HttpServletResponse)servletResponse;
                httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                commonWebComponentService.writeResponse(httpResponse, result.toString());
            }
        }
        catch(ValidatorException e){
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
    }
}
//TODO: duplication between this and AddUpdateUserFilter. maybe create a central RequestFilter
