package com.local.servlet.payment;

import com.local.dao.DAOException;
import com.local.model.Payment;
import com.local.model.User;
import com.local.service.payment.PaymentService;
import com.local.service.usermanagement.UserManagementService;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.servlet.CommonWebComponentService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class GetPendingPaymentServlet extends HttpServlet {
    private PaymentService paymentService;
    private CommonWebComponentService commonWebComponentService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        paymentService = (PaymentService)getServletContext().getAttribute("paymentService");
        commonWebComponentService = (CommonWebComponentService)getServletContext().getAttribute("commonWebComponentService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            int userId = Integer.parseInt(request.getParameter("userId"));
            Payment payment = paymentService.getPendingPayment(userId);
            commonWebComponentService.writeResponse(response, payment);
        }
        catch(NumberFormatException | UserNotFoundException | DAOException e){
            throw new ServletException(e);
        }
    }
}
