package com.sbilhbank.insur.exception;

public class LogicException extends RuntimeException {

    private static final long serialVersionUID = -1100294025073434155L;

    public LogicException(String message, Object... args) {
        super(message);
    }

    public LogicException(Throwable cause, String message, Object... args) {
        super(message, cause);
    }

}
