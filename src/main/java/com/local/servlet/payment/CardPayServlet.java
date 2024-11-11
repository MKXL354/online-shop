package com.local.servlet.payment;

import com.local.dao.DAOException;
import com.local.model.User;
import com.local.service.payment.PaymentService;
import com.local.service.payment.PendingPaymentNotFoundException;
import com.local.service.payment.WebPaymentException;
import com.local.service.usermanagement.UserManagementService;
import com.local.service.usermanagement.UserNotFoundException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CardPayServlet extends HttpServlet {
    private UserManagementService userManagementService;
    private PaymentService paymentService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userManagementService = (UserManagementService)getServletContext().getAttribute("userManagementService");
        paymentService = (PaymentService)getServletContext().getAttribute("paymentService");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try{
            int userId = Integer.parseInt(request.getParameter("userId"));
            User user = userManagementService.getUserById(userId);
            paymentService.cardPay(user);
        }
        catch(NumberFormatException | UserNotFoundException | PendingPaymentNotFoundException | WebPaymentException | DAOException e){
            throw new ServletException(e);
        }
    }
}
