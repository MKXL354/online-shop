package com.local.servlet.payment;

import com.local.dao.DAOException;
import com.local.model.Payment;
import com.local.service.payment.PaymentService;
import com.local.servlet.common.CommonWebComponentService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashSet;

public class GetAllPaymentsServlet extends HttpServlet {
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
//        try {
//            HashSet<Payment> payments = paymentService.getAllPayments();
//            commonWebComponentService.writeResponse(response, null);
//        } catch (DAOException e) {
//            throw new ServletException(e);
//        }
    }
}

//TODO: maybe later
