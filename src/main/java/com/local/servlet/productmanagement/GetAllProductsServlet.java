package com.local.servlet.productmanagement;

import com.local.dao.DAOException;
import com.local.model.Product;
import com.local.servlet.common.CommonWebComponentService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashSet;

public class GetAllProductsServlet extends HttpServlet {
    private ProductManagementService productManagementService;
    private CommonWebComponentService commonWebComponentService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productManagementService = (ProductManagementService)getServletContext().getAttribute("productManagementService");
        commonWebComponentService = (CommonWebComponentService)getServletContext().getAttribute("commonWebComponentService");
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try{
            HashSet<Product> products = productManagementService.getAllProducts();
            commonWebComponentService.writeResponse(response, products);
        }
        catch (DAOException e) {
            throw new ServletException(e);
        }
    }
}
