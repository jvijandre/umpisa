package com.app.justin.reservation.umpisa.exception;

public class BadRequestException extends Exception {

    private static final long serialVersionUID = 1L;

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message,Throwable t) {
        super(message,t);
    }
}
