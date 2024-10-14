package com.local.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class CommonWebComponentService {
    private Gson gson;

    public CommonWebComponentService() {
        gson = new Gson();
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
}
