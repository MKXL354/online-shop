package com.local.servlet.payment;

import com.local.dao.DAOException;
import com.local.exception.service.TransactionException;
import com.local.exception.service.user.PaymentNotPendingException;
import com.local.service.user.UserService;
import com.local.exception.service.usermanagement.UserNotFoundException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CancelPaymentServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userService = (UserService)getServletContext().getAttribute("userService");
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException{
//        TODO: implementation
//        try{
//            int userId = Integer.parseInt(request.getParameter("userId"));
//            userService.rollbackPurchase(userId);
//        }
//        catch(NumberFormatException | UserNotFoundException | PaymentNotPendingException | TransactionException | DAOException e){
//            throw new ServletException(e);
//        }
    }
}
