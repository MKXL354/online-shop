package com.local.servlet.payment;

import com.local.dao.DAOException;
import com.local.model.User;
import com.local.exception.service.TransactionException;
import com.local.exception.service.payment.InsufficientBalanceException;
import com.local.service.payment.PaymentService;
import com.local.exception.service.payment.PendingPaymentNotFoundException;
import com.local.service.usermanagement.UserManagementService;
import com.local.exception.service.usermanagement.UserNotFoundException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class BalancePayServlet extends HttpServlet {
    private PaymentService paymentService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        paymentService = (PaymentService)getServletContext().getAttribute("paymentService");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try{
            int userId = Integer.parseInt(request.getParameter("userId"));
            paymentService.balancePay(userId);
        }
        catch(NumberFormatException | UserNotFoundException | PendingPaymentNotFoundException |
              InsufficientBalanceException | TransactionException | DAOException e){
            throw new ServletException(e);
        }
    }
}
