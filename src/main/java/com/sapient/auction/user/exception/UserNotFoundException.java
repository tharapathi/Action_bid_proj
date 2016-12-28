package com.sapient.auction.user.exception;

import lombok.Getter;

/**
 * Created by dpadal on 11/17/2016.
 */
@Getter
public class UserNotFoundException extends Exception{

    private int statusCode;
    private String message;

    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    public UserNotFoundException(int statusCode, String message) {
        super();
        this.statusCode = statusCode;
        this.message = message;
    }

    public UserNotFoundException(String message, Throwable t) {
        super(message, t);
    }
}
