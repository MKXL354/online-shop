package com.local.servlet.usermanagement;

import com.local.service.UserManagementService;
import com.local.servlet.CommonWebComponentService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DeleteUserServlet extends HttpServlet {
    private UserManagementService userManagementService;
    private CommonWebComponentService commonWebComponentService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userManagementService = (UserManagementService)getServletContext().getAttribute("userManagementService");
        commonWebComponentService = (CommonWebComponentService)getServletContext().getAttribute("commonWebComponentService");
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            int id = (int)request.getAttribute("id");
//            userManagementService.deleteUser(id);
//            commonWebComponentService.writeResponse(response, "success");
//        } catch (UserManagementServiceException e) {
//            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            commonWebComponentService.writeResponse(response, e.getMessage());
//        }
    }
}
