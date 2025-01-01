package com.local.servlet.user;

import com.local.dao.DAOException;
import com.local.exception.service.payment.PendingPaymentNotFoundException;
import com.local.exception.service.usermanagement.UserNotFoundException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CancelPurchaseServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException{
        try{
            int userId = (int)request.getAttribute("userId");
            userService.cancelPurchase(userId);
        }
        catch(UserNotFoundException | PendingPaymentNotFoundException | DAOException e){
            throw new ServletException(e);
        }
    }
}
