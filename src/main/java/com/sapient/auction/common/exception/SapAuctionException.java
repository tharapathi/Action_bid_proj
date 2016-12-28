package com.sapient.auction.common.exception;

import lombok.Getter;

/**
 * Created by dpadal on 11/17/2016.
 */
@Getter
public class SapAuctionException extends Exception {

	private int statusCode;
    private String message;

    public SapAuctionException() {
        super();
    }

    public SapAuctionException(String message) {
        super(message);
        this.message = message;
    }

    public SapAuctionException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
    }

    public SapAuctionException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
