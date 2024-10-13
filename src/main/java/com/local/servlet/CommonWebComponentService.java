package com.local.servlet;

import com.google.gson.Gson;
import com.local.util.token.InvalidTokenException;
import com.local.util.token.TokenExpiredException;
import com.local.util.token.TokenManager;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class CommonWebComponentService {
    private Gson gson;
    private TokenManager tokenManager;

    public CommonWebComponentService(TokenManager tokenManager) {
        gson = new Gson();
        this.tokenManager = tokenManager;
    }

    public <T> T getObjectFromJsonRequest(ServletRequest request, Class<T> objectType) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuilder result = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null){
            result.append(line);
        }
        return gson.fromJson(result.toString(), objectType);
    }

    public void writeResponse(ServletResponse response, String result) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.println(result);
    }

    public void writeResponse(ServletResponse response, Object JsonResult) throws IOException {
        String result = gson.toJson(JsonResult);
        writeResponse(response, result);
    }

    public void validateToken(ServletRequest servletRequest, Map<String, Object> claims) throws InvalidTokenException, TokenExpiredException{
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        String jws = httpServletRequest.getHeader("Authorization");
        tokenManager.validateSignedToken(jws, claims);
    }
}
