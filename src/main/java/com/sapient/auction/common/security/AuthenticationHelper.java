package com.sapient.auction.common.security;

import com.sapient.auction.common.exception.AuthenticationFailedException;
import com.sapient.auction.user.entity.User;

/**
 * Created by dpadal on 11/20/2016.
 */
public final class AuthenticationHelper {

    public static void isRequestAuthenticated(User user) throws AuthenticationFailedException {
        if (user == null) {
            throw new AuthenticationFailedException("Authentication Failed.");
        }
    }
}
