package com.local.servlet.usermanagement;

import com.local.service.UserManagementService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AddUserServlet extends HttpServlet {
    private UserManagementService userManagementService = (UserManagementService)getServletContext().getAttribute("userManagementService");
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        userManagementService.addUser(username, password);
    }
}
