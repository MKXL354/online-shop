package com.local.servlet;

import com.google.gson.*;
import com.local.model.*;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;

public class CommonWebComponentService {
    private class ProductDeserializer implements JsonDeserializer<Product> {
        @Override
        public Product deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String type = jsonObject.get("type").getAsString();

            switch (ProductType.valueOf(type)) {
                case PRODUCT -> {
                    return context.deserialize(json, Product.class);
                }
                case DESKTOP -> {
                    return context.deserialize(json, DesktopProduct.class);
                }
                case LAPTOP -> {
                    return context.deserialize(json, LaptopProduct.class);
                }
                case MOBILE -> {
                    return context.deserialize(json, MobileProduct.class);
                }
                default -> throw new JsonParseException("unknown type: " + type);
            }
        }
    }

    private Gson gson = new GsonBuilder().registerTypeAdapter(Product.class, new ProductDeserializer()).create();

    public CommonWebComponentService() {
        gson = new Gson();
    }

    public <T> T getObjectFromJsonRequest(ServletRequest request, Class<T> objectType) throws IOException, JsonFormatException {
        BufferedReader reader = request.getReader();
        StringBuilder result = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null){
            result.append(line);
        }
        T object;
        try{
            object = gson.fromJson(result.toString(), objectType);
        }
        catch(JsonParseException e){
            throw new JsonFormatException("cannot parse json", e);
        }
        return object;
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
