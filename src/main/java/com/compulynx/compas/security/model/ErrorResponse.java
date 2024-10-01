package com.compulynx.compas.security.model;

public class ErrorResponse {
    private String message;
    private String statusCode;
    private String description;

    public ErrorResponse(){}

    public ErrorResponse(String message, String statusCode, String description) {
        this.message = message;
        this.statusCode = statusCode;
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
