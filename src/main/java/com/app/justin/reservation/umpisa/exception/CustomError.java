package com.app.justin.reservation.umpisa.exception;

import lombok.Data;

@Data
public class CustomError {
    private int errorCode;
    private String message;
}
