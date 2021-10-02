package com.outsider.paymybuddy.exception;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class EmailAlreadyUsedException extends Exception{

    public EmailAlreadyUsedException(String message) {
        super(message);
        log.error(message);
    }
}
