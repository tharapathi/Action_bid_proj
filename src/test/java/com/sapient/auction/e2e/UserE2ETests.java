package com.sapient.auction.e2e;

import com.sapient.auction.common.model.AuctionResponse;
import com.sapient.auction.common.model.ErrorVO;
import com.sapient.auction.common.model.UserVO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.core.Response;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by dpadal on 11/18/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class UserE2ETests extends BaseE2ETest {


    @Before
    public void setUp() {
        super.setUp();
    }


    /**
     * Test User Registration.
     * Expected: success 201 Created.
     */
    @Test
    public void testRegister() {
        AuctionResponse response = registerUser("treddy4@sapient.com");
        assertEquals(response.getStatusCode(), Response.Status.CREATED.getStatusCode());
        assertEquals(response.getMessage(), "User registration successful.");
    }


    /**
     * Test user Register with existing userId.
     * Exptected: failed 400 Bad request.
     */
    @Test
    public void testRegisterWithExistingUserId() {

        //create user1.
        AuctionResponse response = registerUser("treddy4@sapient.com");
        assertEquals(response.getStatusCode(), Response.Status.CREATED.getStatusCode());
        assertEquals(response.getMessage(), "User registration successful.");

        //create user2.
        response = registerUser("treddy4@sapient.com");
        assertEquals(response.getStatusCode(), Response.Status.BAD_REQUEST.getStatusCode());
        assertTrue(response.getErrorVOs().size() > 0);
        for (ErrorVO errorVO : response.getErrorVOs()) {
            assertEquals(errorVO.getMessage(), "user id being used, please try with other.");
        }
    }

    /**
     * Tests user login functionality.
     * Exptected: Success 200 OK.
     */
    @Test
    public void testLogin() {
        //create user1.
        AuctionResponse response = registerUser("treddy41@sapient.com");
        assertEquals(response.getStatusCode(), Response.Status.CREATED.getStatusCode());
        assertEquals(response.getMessage(), "User registration successful.");

        //Login.
        HttpEntity httpEntity = new HttpEntity(user("treddy41@sapient.com"), httpHeaders);
        ResponseEntity responseEntity = restTemplate.exchange(baseURL + "/user/login", HttpMethod.POST, httpEntity, AuctionResponse.class);
        response = (AuctionResponse) responseEntity.getBody();
        assertEquals(response.getStatusCode(), Response.Status.OK.getStatusCode());
        assertEquals(response.getMessage(), "Logged in successful.");
    }

    /**
     * Test login with invalid userid.
     * Expected: failed with 404-NotFound.
     */
    @Test
    public void testLoginFailed() {
        UserVO user = user("treddy41@sapient.com");

        HttpEntity httpEntity = new HttpEntity(user, httpHeaders);
        ResponseEntity responseEntity = restTemplate.exchange(baseURL + "/login", HttpMethod.POST, httpEntity, AuctionResponse.class);
        AuctionResponse response = (AuctionResponse) responseEntity.getBody();
        assertEquals(response.getStatusCode(), Response.Status.NOT_FOUND.getStatusCode());
        assertTrue(response.getErrorVOs().size() > 0);
        for (ErrorVO errorVO : response.getErrorVOs()) {
            assertEquals(errorVO.getMessage(), "User not found.");
        }
    }


    @After
    public void tearDown() {
        super.tearDown();
    }
}
