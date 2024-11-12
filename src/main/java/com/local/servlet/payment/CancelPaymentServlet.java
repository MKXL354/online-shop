package com.local.servlet.payment;

import com.local.dao.DAOException;
import com.local.model.Payment;
import com.local.model.User;
import com.local.exception.service.TransactionException;
import com.local.service.payment.PaymentService;
import com.local.exception.service.user.PaymentNotPendingException;
import com.local.service.user.UserService;
import com.local.service.usermanagement.UserManagementService;
import com.local.exception.service.usermanagement.UserNotFoundException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CancelPaymentServlet extends HttpServlet {
    private UserManagementService userManagementService;
    private UserService userService;
    private PaymentService paymentService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userManagementService = (UserManagementService)getServletContext().getAttribute("userManagementService");
        userService = (UserService)getServletContext().getAttribute("userService");
        paymentService = (PaymentService)getServletContext().getAttribute("paymentService");
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException{
        try{
            int userId = Integer.parseInt(request.getParameter("userId"));
            User user = userManagementService.getUserById(userId);
            Payment payment = paymentService.getPendingPayment(user);
            userService.rollbackPurchase(payment);
        }
        catch(NumberFormatException | UserNotFoundException | PaymentNotPendingException | TransactionException | DAOException e){
            throw new ServletException(e);
        }
    }
}
