package com.app.justin.reservation.umpisa.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j2
public class ControllerExceptionHandler {

    @ExceptionHandler({ MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error("An exception has occurred - {}", exception);

        StringBuilder strBuilder = new StringBuilder();
        exception.getBindingResult().getAllErrors().forEach((e) -> {
            String fieldName;
            try {
                fieldName = ((FieldError) e).getField();

            } catch (ClassCastException cce) {
                fieldName = e.getObjectName();
            }
            String message = e.getDefaultMessage();
            strBuilder.append(String.format("(%s : %s) ", fieldName, message));
        });

        CustomError error = new CustomError();
        error.setErrorCode(HttpStatus.BAD_REQUEST.value());
        error.setMessage("An error occurred - " + strBuilder);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }


    @ExceptionHandler({ HttpMessageNotReadableException.class })
    public ResponseEntity<Object> handleMethodArgumentNotValidException(HttpMessageNotReadableException exception) {
        log.error("An exception has occurred - {}", exception);

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(String.format("(Malformed JSON Request %s) ", exception.getLocalizedMessage()));

        CustomError error = new CustomError();
        error.setErrorCode(HttpStatus.BAD_REQUEST.value());
        error.setMessage("An error occurred - " + strBuilder);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler({ ReservationException.class })
    public ResponseEntity<Object> handleReservationException(ReservationException exception) {
        log.error("An exception has occurred - {}", exception);
        CustomError error = new CustomError();
        error.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setMessage("An error occurred - " + exception.getLocalizedMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    @ExceptionHandler({ BadRequestException.class })
    public ResponseEntity<Object> handleBadRequestException(BadRequestException exception) {
        log.error("An exception has occurred - {}", exception);
        CustomError error = new CustomError();
        error.setErrorCode(HttpStatus.BAD_REQUEST.value());
        error.setMessage("An error occurred - " + exception.getLocalizedMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleGlobalException(Exception exception) {
        log.error("An exception has occurred - {}", exception);
        CustomError error = new CustomError();
        error.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setMessage("An error occurred - " + exception.getLocalizedMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}
