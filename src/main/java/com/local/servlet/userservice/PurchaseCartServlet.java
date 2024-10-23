package com.local.servlet.userservice;

import com.local.dao.DAOException;
import com.local.model.Cart;
import com.local.model.User;
import com.local.service.user.UserService;
import com.local.service.usermanagement.UserManagementService;
import com.local.service.usermanagement.UserNotFoundException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PurchaseCartServlet extends HttpServlet {
    private UserManagementService userManagementService;
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userManagementService = (UserManagementService) getServletContext().getAttribute("userManagementService");
        userService = (UserService)getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        try{
            User user = userManagementService.getUserById(userId);
            Cart cart = userService.getCart(user);
//            TODO: purchaseCart
//            userService.purchaseCart(cart);
        }
        catch(UserNotFoundException | DAOException e){
            throw new ServletException(e);
        }
    }
}
