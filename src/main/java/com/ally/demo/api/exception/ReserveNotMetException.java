package com.ally.demo.api.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = false)
public class ReserveNotMetException extends Exception {
    private static final long serialVersionUID = 1L;
    HttpStatus status;

    public ReserveNotMetException(String exception, HttpStatus status) {
        super(exception);
        this.status =  status;
    }
}
