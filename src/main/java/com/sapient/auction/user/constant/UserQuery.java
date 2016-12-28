package com.sapient.auction.user.constant;

/**
 * Created by dpadal on 11/17/2016.
 */
public final class UserQuery {
    public static final String IS_USER_ID_ALREADY_EXIST = "SELECT count(ID) FROM USER WHERE email = :email";
    public static final String IS_AUTHENTICATED = "from User where email = :email and password = :password";
    public static final String IS_USER_AUTHENTICATED = "SELECT user.* FROM USER user WHERE user.ID = :userId and user.PASSWORD = :password";
    public static final String USER_BYEMAIL = "from User WHERE email = :email";
}
