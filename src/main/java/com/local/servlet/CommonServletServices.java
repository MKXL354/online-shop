package com.local.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class CommonServletServices {
    private static Gson gson = new Gson();

    public <T> T getObjectFromJsonRequest(ServletRequest request, Class<T> objectType) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuilder result = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null){
            result.append(line);
        }
        return gson.fromJson(result.toString(), objectType);
    }

    public void writeResponse(HttpServletResponse response, String result) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.println(result);
    }

    public void writeResponse(HttpServletResponse response, Object JsonResult) throws IOException {
        String result = gson.toJson(JsonResult);
        writeResponse(response, result);
    }
}
