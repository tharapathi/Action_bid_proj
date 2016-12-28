package com.sapient.auction.user.dao;

import com.sapient.auction.SapAuctionSiteApplication;
import com.sapient.auction.user.entity.User;
import com.sapient.auction.user.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by dpadal on 11/18/2016.
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SapAuctionSiteApplication.class)
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Before
    public void before() {

    }

    @Test
    public void testUserRepository() {
        assertNotNull(userRepository);
    }

    @Test
    public void testCreateUser() {
        userRepository.register(new User());
    }

    @Test
    public void testGetUserByEmail() {
        User user = userRepository.register(user("treddy4@sapient.com"));
        assertNotNull(user.getId());
        Optional<User> userOptonal = userRepository.getUserByEmail("treddy4@sapient.com");
        assertTrue(userOptonal.isPresent());
    }

    private User user(String email) {
        User user = new User();
        user.setPassword("123456789");
        user.setFirstName("tharapathi");
        user.setLastName("reddy");
        user.setContact("7405207197");
        user.setEmail(email);
        user.setAddress("Bangalore");

        return user;
    }

    @After
    public void tearDown() {
        userRepository = null;
    }
}
