package com.sapient.auction.user.exception;

import lombok.Getter;

/**
 * Exception raised when the userid already exist in DB.
 *
 * Created by dpadal on 11/17/2016.
 */
@Getter
public class UserAlreadyExistException extends  Exception {
    private int statusCode;
    private String message;

    public UserAlreadyExistException() {
        super();
    }

    public UserAlreadyExistException(String message) {
        super(message);
        this.message = message;
    }

    public UserAlreadyExistException(int statusCode, String message) {
        super();
        this.statusCode = statusCode;
        this.message = message;
    }

    public UserAlreadyExistException(String message, Throwable t) {
        super(message, t);
    }
}
