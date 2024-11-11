package com.local.servlet.user;

import com.local.dao.DAOException;
import com.local.model.User;
import com.local.service.TransactionException;
import com.local.service.user.EmptyCartException;
import com.local.service.user.InsufficientProductCountException;
import com.local.service.user.PreviousPaymentPendingException;
import com.local.service.user.UserService;
import com.local.service.usermanagement.UserManagementService;
import com.local.service.usermanagement.UserNotFoundException;
import com.local.servlet.CommonWebComponentService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class FinalizePurchaseServlet extends HttpServlet {
    private CommonWebComponentService commonWebComponentService;
    private UserManagementService userManagementService;
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        commonWebComponentService = (CommonWebComponentService)getServletContext().getAttribute("commonWebComponentService");
        userManagementService = (UserManagementService)getServletContext().getAttribute("userManagementService");
        userService = (UserService)getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try{
            int userId = Integer.parseInt(request.getParameter("userId"));
            User user = userManagementService.getUserById(userId);
            int paymentId = userService.finalizePurchase(user);
            commonWebComponentService.writeResponse(response, paymentId);
        }
        catch(NumberFormatException | UserNotFoundException | PreviousPaymentPendingException | EmptyCartException |
              InsufficientProductCountException | TransactionException | DAOException e){
            throw new ServletException(e);
        }
    }
}
