package com.sipc.xxsc.exception;
@SkipNotice
public class ErrorException extends RuntimeException{
    public ErrorException(String message) {
        super(message);
    }
}
