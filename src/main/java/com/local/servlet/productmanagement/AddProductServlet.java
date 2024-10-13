package com.local.servlet.productmanagement;

import com.local.model.Product;
import com.local.service.ProductManagementService;
import com.local.service.ProductManagementServiceException;
import com.local.servlet.CommonWebComponentService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AddProductServlet extends HttpServlet {
    private ProductManagementService productManagementService;
    private CommonWebComponentService commonWebComponentService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productManagementService = (ProductManagementService)getServletContext().getAttribute("productManagementService");
        commonWebComponentService = (CommonWebComponentService)getServletContext().getAttribute("commonWebComponentService");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Product product = (Product)request.getAttribute("product");
        try {
            productManagementService.addProduct(product);
            commonWebComponentService.writeResponse(response, "success");
        } catch (ProductManagementServiceException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            commonWebComponentService.writeResponse(response, e.getMessage());
        }
    }
}
