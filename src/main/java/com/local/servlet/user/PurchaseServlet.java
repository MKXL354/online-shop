package com.local.servlet.user;

import com.local.dao.DAOException;
import com.local.model.User;
import com.local.service.TransactionException;
import com.local.service.user.EmptyCartException;
import com.local.service.user.InsufficientBalanceException;
import com.local.service.user.InsufficientProductCountException;
import com.local.service.user.UserService;
import com.local.service.usermanagement.UserManagementService;
import com.local.service.usermanagement.UserNotFoundException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PurchaseServlet extends HttpServlet {
    private UserManagementService userManagementService;
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userManagementService = (UserManagementService)getServletContext().getAttribute("userManagementService");
        userService = (UserService)getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try{
            int userId = Integer.parseInt(request.getParameter("userId"));
            User user = userManagementService.getUserById(userId);
            userService.balancePurchase(user);
        }
        catch(NumberFormatException | UserNotFoundException | EmptyCartException | InsufficientBalanceException |
              InsufficientProductCountException | TransactionException | DAOException e){
            throw new ServletException(e);
        }
    }
}
