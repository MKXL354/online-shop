package com.local.servlet.usermanagement;

import com.local.model.User;
import com.local.service.ServiceException;
import com.local.service.UserManagementService;
import com.local.servlet.CommonServletServices;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class UpdateUserServlet extends HttpServlet {
    private UserManagementService userManagementService;
    private CommonServletServices commonServletServices;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userManagementService = (UserManagementService)getServletContext().getAttribute("userManagementService");
        commonServletServices = (CommonServletServices)getServletContext().getAttribute("commonServletServices");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User)request.getAttribute("user");
        try {
            userManagementService.updateUser(user);
            commonServletServices.writeResponse(response, "success");
        } catch (ServiceException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            commonServletServices.writeResponse(response, e.getMessage());
        }
    }
}
