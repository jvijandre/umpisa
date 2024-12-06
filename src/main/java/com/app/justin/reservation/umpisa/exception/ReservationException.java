package com.app.justin.reservation.umpisa.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ReservationException extends Exception {

    private static final long serialVersionUID = 1L;

    public ReservationException(String message) {
        super(message);
    }

    public ReservationException(String message,Throwable t) {
        super(message,t);
    }
}
