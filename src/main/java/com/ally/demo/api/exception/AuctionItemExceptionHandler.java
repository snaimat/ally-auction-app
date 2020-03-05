package com.ally.demo.api.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;

@Slf4j
@ControllerAdvice(basePackages = "com.ally.demo.api")
@RestController
public class AuctionItemExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<String> handleGeneralException(Exception ex) {
        log.error("Exception", ex.getMessage(),ex);
        return new ResponseEntity<>("Something's not right ... handling error.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SQLException.class)
    public final ResponseEntity<String> handleSqlException(Exception ex){
        log.error("SQL Exception", ex.getMessage(), ex);
        return new ResponseEntity<>("Something's not right with the DB call ... handling error.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MissingRequiredParameterException.class)
    public final ResponseEntity<String> handleMissingRequiredParameterException(Exception ex){
        log.error("MissingRequiredParameterException", ex.getMessage(), ex);
        return new ResponseEntity<>("Something's not right - did you miss the path variable...", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ItemIdNotFoundException.class)
    public final ResponseEntity<String> handleItemIdNotFoundException(Exception ex) {
        log.error("ItemIdNotFoundException", ex.getMessage(),ex);
        return new ResponseEntity<>("No hits.", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReserveNotMetException.class)
    public final ResponseEntity<String> handleReserveNotMetException(Exception ex) {
        log.error("ReserveNotMetException", ex.getMessage(),ex);
        return new ResponseEntity<>("Reserve Not met.", HttpStatus.OK);
    }

}
