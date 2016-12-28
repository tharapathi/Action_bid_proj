package com.sapient.auction.user.service;

import com.sapient.auction.user.entity.User;
import com.sapient.auction.user.exception.UserAlreadyExistException;
import com.sapient.auction.user.exception.UserNotFoundException;
import com.sapient.auction.user.repository.UserRepository;
import com.sapient.auction.user.service.impl.UserServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;

/**
 * Unit tests for the UserService.java to test the business, by Mock the UserRepository.
 *
 * Created by dpadal on 11/18/2016.
 */
@RunWith(JUnit4.class)
public class UserServiceTest {


    @InjectMocks
    private UserService userService = new UserServiceImpl();

    @Mock
    private UserRepository userRepository;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(userRepository.isUserAlreadyExist(anyString())).thenAnswer(invocationOnMock -> {
            String userId = invocationOnMock.getArgumentAt(0, String.class);
            if (userId.endsWith("Exist")) {
                return true;
            } else {
                return false;
            }
        });

        Mockito.when(userRepository.login(anyString(), anyString())).thenAnswer(invocationOnMock -> {
            String userId = invocationOnMock.getArgumentAt(0, String.class); ;
            if (userId.endsWith("NotExist")) {
                return null;
            } else {
                return new User();
            }
        });
    }

    @Test
    public void testRegister() throws UserAlreadyExistException, UserNotFoundException, NoSuchAlgorithmException {
        User fakeUser = fakeUser("treddy4");
        userService.register(fakeUser);
        User user =  userService.login(fakeUser);
        assertNotNull(user);
    }

    @Test(expected = UserAlreadyExistException.class)
    public void testRegisterFailed() throws UserAlreadyExistException, NoSuchAlgorithmException {
        userService.register(fakeUser("dpadalaExist"));
    }

    @Test
    public void testLogin() throws UserNotFoundException {
        User user = userService.login(fakeUser("treddy4"));
        assertNotNull(user);
    }

    @Test(expected = UserNotFoundException.class)
    public void testLoginFailed() throws UserNotFoundException {
        User user = userService.login(fakeUser("dpadalNotExist"));
        assertNull(user);
    }


    private User fakeUser(String userId) {
        User user = new User();
        user.setId(userId);
        user.setPassword("123456789");
        user.setFirstName("tharapathi");
        user.setLastName("reddy");
        user.setContact("7405207197");
        user.setEmail("treddy4@sapient.com");
        user.setAddress("Bangalore");

        return user;
    }


    @After
    public void tearDown() {
        userService = null;
        userRepository = null;
    }
}
