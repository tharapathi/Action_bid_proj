package com.sapient.auction.user.service.impl;

import com.sapient.auction.user.entity.User;
import com.sapient.auction.user.exception.UserAlreadyExistException;
import com.sapient.auction.user.exception.UserNotFoundException;
import com.sapient.auction.user.repository.UserRepository;
import com.sapient.auction.user.service.UserService;
import com.sapient.auction.user.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;

/**
 * Created by dpadal on 11/11/2016.
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Register user.
     *
     * @param user
     * @throws UserAlreadyExistException
     * @throws NoSuchAlgorithmException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void register(User user) throws UserAlreadyExistException, NoSuchAlgorithmException {
        if(!userRepository.isUserAlreadyExist(user.getEmail())) {
            user.setPassword(PasswordUtil.hash(user.getPassword()));
            user = userRepository.register(user);
            log.info("User {} stored in db with id: {}", user.getId());
        } else {
            throw new UserAlreadyExistException("user id being used, please try with other.");
        }
    }

    /**
     * User login.
     *
     * @param user
     * @return
     * @throws UserNotFoundException
     */
    @Override
    @Transactional(readOnly = true)
    public User login(User user) throws UserNotFoundException {
        user = userRepository.login(user.getEmail(), user.getPassword());
        if (user == null) {
            throw new UserNotFoundException(String.format("User not found."));
        }
        return user;
    }
}
