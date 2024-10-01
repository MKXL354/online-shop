package com.local.servlet.usermanagement;

import com.local.commonexceptions.ServiceException;
import com.local.service.UserManagementService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AddUserServlet extends HttpServlet {
    private UserManagementService userManagementService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userManagementService = (UserManagementService)getServletContext().getAttribute("userManagementService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        try {
            userManagementService.addUser(username, password);
        } catch (ServiceException e) {
//            TODO: exception handling related to response
            e.printStackTrace();
        }
    }
}
