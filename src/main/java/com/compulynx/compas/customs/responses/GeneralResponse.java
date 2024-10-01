package com.compulynx.compas.customs.responses;

import org.springframework.stereotype.Component;

public class GeneralResponse {
    private String statusCode;
    private String message;

    public GeneralResponse(String statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
