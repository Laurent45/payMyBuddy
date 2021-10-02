package com.outsider.paymybuddy.exception;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class AmountTransferException extends Exception {

    public AmountTransferException(String message) {
        super(message);
        log.error(message);
    }
}
