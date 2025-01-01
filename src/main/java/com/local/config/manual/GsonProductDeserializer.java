package com.local.config.manual;

import com.google.gson.*;
import com.local.model.*;

import java.lang.reflect.Type;

public class GsonProductDeserializer implements JsonDeserializer<Product> {
    @Override
    public Product deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("productType").getAsString();

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
