package com.app.jpr.exceptions;

import lombok.Data;

@Data
public class ErrorResponse {
    private final int statusCode;
    private final String message;
}
