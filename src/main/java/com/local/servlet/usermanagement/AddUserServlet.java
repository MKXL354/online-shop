package com.local.servlet.usermanagement;

import com.google.gson.Gson;
import com.local.model.User;
import com.local.service.ServiceException;
import com.local.service.UserManagementService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

public class AddUserServlet extends HttpServlet {
    private UserManagementService userManagementService;
    private Gson gson = new Gson();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userManagementService = (UserManagementService)getServletContext().getAttribute("userManagementService");
    }

    private User readUserFromRequest(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null){
            stringBuilder.append(line);
        }
        return this.gson.fromJson(stringBuilder.toString(), User.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = readUserFromRequest(request);
        try {
            userManagementService.addUser(user);
        } catch (ServiceException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            TODO: exception handling related to response
//            TODO: specific service exception and added general application exception
//            TODO: layered exception for Web, Service, DAO
            e.printStackTrace();
        }
    }
}
