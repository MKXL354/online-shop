package com.local.servlet.productmanagement;

import com.local.dao.DAOException;
import com.local.model.Product;
import com.local.service.productmanagement.*;
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Product product = (Product)request.getAttribute("product");
        try {
            product = productManagementService.addProduct(product);
            commonWebComponentService.writeResponse(response, product);
        } catch (InvalidProductCountException | InvalidProductPriceException | DuplicateProductNameException | DAOException e) {
            throw new ServletException(e);
        }
    }
}
