package com.local.servlet.user;

import com.local.dao.DAOException;
import com.local.model.Cart;
import com.local.service.user.UserService;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.servlet.CommonWebComponentService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class GetActiveCartServlet extends HttpServlet {
    private UserService userService;
    private CommonWebComponentService commonWebComponentService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userService = (UserService)getServletContext().getAttribute("userService");
        commonWebComponentService = (CommonWebComponentService)getServletContext().getAttribute("commonWebComponentService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            Cart cart =  userService.getActiveCart(userId);
            commonWebComponentService.writeResponse(response, cart);
        }
        catch (NumberFormatException | UserNotFoundException | DAOException e) {
            throw new ServletException(e);
        }
    }
}

