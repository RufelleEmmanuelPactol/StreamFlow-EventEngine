package com.streamflow.errors;

public class ClientErrors extends RuntimeException{

    public ClientErrors() {
        super();
    }

    public ClientErrors(String message) {
        super(message);
    }

    public ClientErrors(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientErrors(Throwable cause) {
        super(cause);
    }

    protected ClientErrors(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

