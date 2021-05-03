package com.example.email.exception;

import java.io.IOException;

/**
 * @author Kiran
 * @since 4/30/2021
 */
public class  CustomEmailException extends IOException {
    private final int httpStatus;

    public CustomEmailException(int httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
    public int getHttpStatus() {
        return httpStatus;
    }
}
