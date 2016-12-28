package com.sapient.auction.sale.exception;

/**
 * Exception occurred when the request sale id not found in db.
 *
 * Created by Lovababu on 11/19/2016.
 */
public class SaleNotFoundException extends Exception {
    private int statusCode;
    private String message;

    public SaleNotFoundException() {
        super();
    }

    public SaleNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    public SaleNotFoundException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
    }

    public SaleNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }
}
