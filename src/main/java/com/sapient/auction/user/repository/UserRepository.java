package com.sapient.auction.user.repository;

import java.util.Optional;

import com.sapient.auction.user.entity.User;

/**
 * JPA repository class, to perform  all User related CRUD operations and lookup.
 *
 * Created by dpadal on 11/11/2016.
 */

public interface UserRepository {

    User register(User user);

    User login(String userId, String password);

    boolean isUserAlreadyExist(String userId);
    
    Optional<User> getUserByEmail(String email);
}
