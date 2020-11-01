package com.rent.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

public class ResponseBase {
    private HttpServletResponse response = null;

    ServletOutputStream os = null;

    protected ResponseBase() {
        this
                .response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        try {
            this.os = this.response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected ResponseBase setHeader(String headerName, String headerVal) {
        this.response.setHeader(headerName, headerVal);
        return this;
    }

    protected ResponseBase setContentType(String contentType) {
        this.response.setContentType(contentType);
        return this;
    }

    protected void sendRedirect(String url) {
        try {
            this.response.setStatus(302);
            this.response.addHeader("location", url);
            this.response.sendRedirect(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void write(InputStream is) {
        try {
            int length = 0;
            byte[] buffer = new byte[2048];
            while ((length = is.read(buffer)) != -1)
                this.os.write(buffer, 0, length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void write(byte[] bytes) {
        try {
            this.os.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void write(String message) {
        write(message.getBytes());
    }
}
