package com.sapient.auction.common.security.session;

import com.sapient.auction.user.entity.User;

/**
 * Created by Lovababu on 11/20/2016.
 */
public class SessionUser {

    private static final ThreadLocal<User> sessionUser = new ThreadLocal<User>() {
        @Override
        protected User initialValue() {
            return null;
        }
    };

    public static User getSessionUser() {
        return sessionUser.get();
    }

    public static void setSessionUser(User user) {
        sessionUser.set(user);
    }

}
