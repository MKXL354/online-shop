package com.local.servlet.user;

import com.local.dao.DAOException;
import com.local.exception.service.productmanagement.ProductNotFoundException;
import com.local.exception.service.user.PreviousPaymentPendingException;
import com.local.service.user.UserService;
import com.local.exception.service.usermanagement.UserNotFoundException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AddProductToCartServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userService = (UserService)getServletContext().getAttribute("userService");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            String productName = request.getParameter("productName");
            userService.addProductToCart(userId, productName);
        }
        catch (NumberFormatException | UserNotFoundException | ProductNotFoundException |
               PreviousPaymentPendingException | DAOException e) {
            throw new ServletException(e);
        }
    }
}
