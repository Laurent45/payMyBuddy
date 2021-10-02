package com.outsider.paymybuddy.exception;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ConstraintErrorException extends Exception{

    public ConstraintErrorException(String message) {
        super(message);
        log.error(message);
    }
}
