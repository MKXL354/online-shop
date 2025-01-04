package com.local.web.auth;

import com.local.model.UserType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserAuthHandlerRegistry {
    private Map<UserType, UserAuthHandler> handlerMap;

    public UserAuthHandlerRegistry(List<UserAuthHandler> handlers) {
        handlerMap = new HashMap<>();
        for (UserAuthHandler handler : handlers) {
            handlerMap.put(handler.getUserType(), handler);
        }
    }

    public UserAuthHandler getUserAuthHandler(UserType type) {
        return handlerMap.get(type);
    }
}
