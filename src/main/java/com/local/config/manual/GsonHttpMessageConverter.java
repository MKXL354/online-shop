package com.local.config.manual;

import com.google.gson.Gson;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class GsonHttpMessageConverter extends AbstractHttpMessageConverter {
    private Gson gson;

    public GsonHttpMessageConverter(Gson gson) {
        super(new MediaType("application", "json"));
        this.gson = gson;
    }

    @Override
    protected boolean supports(Class clazz) {
        return true;
    }

    @Override
    protected Object readInternal(Class clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return gson.fromJson(new InputStreamReader(inputMessage.getBody()), clazz);
    }

    @Override
    protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        try (OutputStream outputStream = outputMessage.getBody();
             OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
            gson.toJson(obj, obj.getClass(), writer);
            writer.flush();
        }
    }
}
