package com.local.servlet.user;

import com.local.dao.DAOException;
import com.local.model.User;
import com.local.exception.service.TransactionException;
import com.local.exception.service.user.EmptyCartException;
import com.local.exception.service.user.InsufficientProductCountException;
import com.local.exception.service.user.PreviousPaymentPendingException;
import com.local.service.user.UserService;
import com.local.service.usermanagement.UserManagementService;
import com.local.exception.service.usermanagement.UserNotFoundException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FinalizePurchaseServlet extends HttpServlet {
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
            userService.finalizePurchase(user);
        }
        catch(NumberFormatException | UserNotFoundException | PreviousPaymentPendingException | EmptyCartException |
              InsufficientProductCountException | TransactionException | DAOException e){
            throw new ServletException(e);
        }
    }
}
