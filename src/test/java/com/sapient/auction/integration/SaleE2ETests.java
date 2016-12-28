package com.sapient.auction.integration;

import com.sapient.auction.common.model.AuctionResponse;
import com.sapient.auction.common.model.BidVO;
import com.sapient.auction.common.model.ErrorVO;
import com.sapient.auction.common.model.SaleVO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * E2E test cases for Sale Module.
 * <p>
 * Created by dpadal on 11/19/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class SaleE2ETests extends BaseE2ETest {

    @Before
    public void setUp() {
        super.setUp();
    }


    /**
     * Test Sale create.
     * Expected: success 201 Created.
     */
    @Test
    public void testCreate() throws NoSuchAlgorithmException {
        String email = "treddy4_sale1@sapient.com";
        //ceate user .
        AuctionResponse response = registerUser(email);
        assertEquals(response.getStatusCode(), Response.Status.CREATED.getStatusCode());
        assertEquals(response.getMessage(), "User registration successful.");

        //setting auth header.
        setAuthHeader(email, "password1");
        //create sale.
        SaleVO saleVO = sale(Optional.of(email));
        HttpEntity<SaleVO> httpEntity = new HttpEntity<>(saleVO, httpHeaders);
        ResponseEntity<AuctionResponse> responseEntity = restTemplate.exchange(baseURL.concat("/sale"), HttpMethod.POST, httpEntity, AuctionResponse.class);
        response = responseEntity.getBody();
        assertEquals(response.getStatusCode(), Response.Status.CREATED.getStatusCode());
        assertNotNull(responseEntity.getHeaders().getLocation());
    }

    /**
     * Test Sale create.
     * Expected: Failed 401 UnAuthorized.
     */
    @Test
    public void testCreateUnAuthorized() throws NoSuchAlgorithmException {
        String email = "treddy4_sale2@sapient.com";
        //ceate user .
        AuctionResponse response = registerUser(email);
        assertEquals(response.getStatusCode(), Response.Status.CREATED.getStatusCode());
        assertEquals(response.getMessage(), "User registration successful.");

        //setting auth header.
        setAuthHeader(email, "invalidpassword");
        //create sale.
        SaleVO saleVO = sale(Optional.of(email));
        HttpEntity<SaleVO> httpEntity = new HttpEntity<>(saleVO, httpHeaders);
        ResponseEntity<AuctionResponse> responseEntity = restTemplate.exchange(baseURL.concat("/sale"), HttpMethod.POST, httpEntity, AuctionResponse.class);
        response = responseEntity.getBody();
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatusCode());
        assertTrue(response.getErrorVOs().size() > 0);
        for (ErrorVO errorVO : response.getErrorVOs()) {
            assertEquals(errorVO.getMessage(), "Authentication failed.");
        }
    }

    /**
     * Test Sale Detail.
     * Expected: success 200 Created.
     */
    @Test
    public void testDetail() throws NoSuchAlgorithmException {
        String email = "treddy4_sale3@sapient.com";
        //Create user .
        AuctionResponse response = registerUser(email);
        assertEquals(response.getStatusCode(), Response.Status.CREATED.getStatusCode());
        assertEquals(response.getMessage(), "User registration successful.");

        //setting auth header.
        setAuthHeader(email, "password1");
        //Create sale.
        SaleVO saleVO = sale(Optional.of(email));
        HttpEntity<SaleVO> httpEntity = new HttpEntity<>(saleVO, httpHeaders);
        ResponseEntity<AuctionResponse> createResponse = restTemplate.exchange(baseURL.concat("/sale"), HttpMethod.POST, httpEntity, AuctionResponse.class);
        response = createResponse.getBody();
        assertEquals(response.getStatusCode(), Response.Status.CREATED.getStatusCode());
        assertNotNull(createResponse.getHeaders().getLocation());

        //Get Sale.
        HttpEntity getHttpEntity = new HttpEntity(httpHeaders);
        ResponseEntity<AuctionResponse> getResponseEntity = restTemplate.exchange(createResponse.getHeaders().getLocation(),
                HttpMethod.GET, getHttpEntity, AuctionResponse.class);
        response = getResponseEntity.getBody();
        assertEquals(response.getStatusCode(), Response.Status.OK.getStatusCode());
        assertNotNull(response.getSaleVOs().size() > 0);
        for (SaleVO s : response.getSaleVOs()) {
            assertNotNull(s.getId());
            assertEquals(s.getUser().getEmail(), saleVO.getUser().getEmail());
            assertEquals(s.getProductId(), saleVO.getProductId());
        }
    }

    /**
     * Test Sale Detail.
     * Expected: failed 401 UnAuthorized.
     */
    @Test
    public void testDetailUnAuthorized() throws NoSuchAlgorithmException {
        String email = "treddy4_sale4@sapient.com";
        //Create user .
        AuctionResponse response = registerUser(email);
        assertEquals(response.getStatusCode(), Response.Status.CREATED.getStatusCode());
        assertEquals(response.getMessage(), "User registration successful.");

        //setting auth header.
        setAuthHeader(email, "password1");
        //Create sale.
        SaleVO saleVO = sale(Optional.of(email));
        HttpEntity<SaleVO> httpEntity = new HttpEntity<>(saleVO, httpHeaders);
        ResponseEntity<AuctionResponse> createResponse = restTemplate.exchange(baseURL.concat("/sale"), HttpMethod.POST, httpEntity, AuctionResponse.class);
        response = createResponse.getBody();
        assertEquals(response.getStatusCode(), Response.Status.CREATED.getStatusCode());
        assertNotNull(createResponse.getHeaders().getLocation());

        //setting auth header.
        setAuthHeader(email, "invalidPassword");
        //Get Sale.
        HttpEntity getHttpEntity = new HttpEntity(httpHeaders);
        ResponseEntity<AuctionResponse> getResponseEntity = restTemplate.exchange(createResponse.getHeaders().getLocation(),
                HttpMethod.GET, getHttpEntity, AuctionResponse.class);
        response = getResponseEntity.getBody();
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatusCode());
        assertTrue(response.getErrorVOs().size() > 0);
        for (ErrorVO errorVO : response.getErrorVOs()) {
            assertEquals(errorVO.getMessage(), "Authentication failed.");
        }
    }

    /**
     * Test Sale List.
     * Expected: success 200 Created.
     */
    @Test
    public void testSaleList() throws NoSuchAlgorithmException {
        String email = "treddy4_sale5@sapient.com";
        //Create user .
        AuctionResponse response = registerUser(email);
        assertEquals(response.getStatusCode(), Response.Status.CREATED.getStatusCode());
        assertEquals(response.getMessage(), "User registration successful.");

        //setting auth header.
        setAuthHeader(email, "password1");
        //Create sale1.
        SaleVO saleVO = sale(Optional.of(email));
        HttpEntity<SaleVO> httpEntity = new HttpEntity<>(saleVO, httpHeaders);
        ResponseEntity<AuctionResponse> createResponse = restTemplate.exchange(baseURL.concat("/sale"), HttpMethod.POST, httpEntity, AuctionResponse.class);
        response = createResponse.getBody();
        assertEquals(response.getStatusCode(), Response.Status.CREATED.getStatusCode());
        assertNotNull(createResponse.getHeaders().getLocation());

        //Create sale2.
        httpEntity = new HttpEntity<>(saleVO, httpHeaders);
        createResponse = restTemplate.exchange(baseURL.concat("/sale"), HttpMethod.POST, httpEntity, AuctionResponse.class);
        response = createResponse.getBody();
        assertEquals(response.getStatusCode(), Response.Status.CREATED.getStatusCode());
        assertNotNull(createResponse.getHeaders().getLocation());

        //List Sale.
        HttpEntity getHttpEntity = new HttpEntity(httpHeaders);
        ResponseEntity<AuctionResponse> getResponseEntity = restTemplate.exchange(baseURL.concat("/sale/list"),
                HttpMethod.GET, getHttpEntity, AuctionResponse.class);
        response = getResponseEntity.getBody();
        assertEquals(response.getStatusCode(), Response.Status.OK.getStatusCode());
        assertNotNull(response.getSaleVOs().size() == 2);
        for (SaleVO s : response.getSaleVOs()) {
            assertNotNull(s.getId());
            assertNotNull(s.getUser());
            assertEquals(s.getUser().getEmail(), saleVO.getUser().getEmail());
            assertEquals(s.getProductId(), saleVO.getProductId());
            assertEquals(s.getBids().size(), 0);
        }
    }

    /**
     * Test Sale List.
     * Expected: Failed 401 UnAuthorized.
     */
    @Test
    public void testSaleListFailed() throws NoSuchAlgorithmException {
        String email = "treddy4_sale6@sapient.com";
        //Create user .
        AuctionResponse response = registerUser(email);
        assertEquals(response.getStatusCode(), Response.Status.CREATED.getStatusCode());
        assertEquals(response.getMessage(), "User registration successful.");

        //set auth header.
        setAuthHeader(email, "invalidpassword");
        //List Sale.
        HttpEntity getHttpEntity = new HttpEntity(httpHeaders);
        ResponseEntity<AuctionResponse> getResponseEntity = restTemplate.exchange(baseURL.concat("/sale/list"),
                HttpMethod.GET, getHttpEntity, AuctionResponse.class);
        response = getResponseEntity.getBody();
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatusCode());
        assertTrue(response.getErrorVOs().size() > 0);
        for (ErrorVO errorVO : response.getErrorVOs()) {
            assertEquals(errorVO.getMessage(), "Authentication failed.");
        }
    }

    /**
     * Test Bid create.
     * Expected: success 201 Created.
     */
    @Test
    public void testBid() throws NoSuchAlgorithmException {
        String email = "treddy4_bid1@sapient.com";
        //ceate user .
        AuctionResponse response = registerUser(email);
        assertEquals(response.getStatusCode(), Response.Status.CREATED.getStatusCode());
        assertEquals(response.getMessage(), "User registration successful.");

        //setting auth header.
        setAuthHeader(email, "password1");
        //create sale.
        SaleVO saleVO = sale(Optional.of(email));
        HttpEntity<SaleVO> httpEntity = new HttpEntity<>(saleVO, httpHeaders);
        ResponseEntity<AuctionResponse> responseEntity = restTemplate.exchange(baseURL.concat("/sale"), HttpMethod.POST, httpEntity, AuctionResponse.class);
        response = responseEntity.getBody();
        assertEquals(response.getStatusCode(), Response.Status.CREATED.getStatusCode());
        assertNotNull(responseEntity.getHeaders().getLocation());

        BidVO bidVO = bid(email, response.getSaleVOs().stream().findFirst().get().getId(), 1000);
        HttpEntity<BidVO> bidHttpEntity = new HttpEntity<>(bidVO, httpHeaders);
        responseEntity = restTemplate.exchange(
                baseURL.concat("/sale/").concat(bidVO.getSale().getId().toString()).concat("/bid"),
                HttpMethod.POST, bidHttpEntity, AuctionResponse.class);
        response = responseEntity.getBody();
        assertEquals(response.getStatusCode(), Response.Status.CREATED.getStatusCode());
        assertNotNull(responseEntity.getHeaders().getLocation());
        assertEquals(response.getMessage(), "Bid posted successfully.");
        assertNotNull(response.getBidVO());
    }

    /**
     * Test Bid create.
     * Expected: Failed 400 BadRequest.
     */
    @Test
    public void testBidWhenSaleIdInvalid() throws NoSuchAlgorithmException {
        String email = "treddy4_bid2@sapient.com";
        //ceate user .
        AuctionResponse response = registerUser(email);
        assertEquals(response.getStatusCode(), Response.Status.CREATED.getStatusCode());
        assertEquals(response.getMessage(), "User registration successful.");

        //setting auth header.
        setAuthHeader(email, "password1");
        //create sale.
        SaleVO saleVO = sale(Optional.of(email));
        HttpEntity<SaleVO> httpEntity = new HttpEntity<>(saleVO, httpHeaders);
        ResponseEntity<AuctionResponse> responseEntity = restTemplate.exchange(baseURL.concat("/sale"), HttpMethod.POST, httpEntity, AuctionResponse.class);
        response = responseEntity.getBody();
        assertEquals(response.getStatusCode(), Response.Status.CREATED.getStatusCode());
        assertNotNull(responseEntity.getHeaders().getLocation());

        //post bid.
        BidVO bidVO = bid(email, 0L, 1000);
        HttpEntity<BidVO> bidHttpEntity = new HttpEntity<>(bidVO, httpHeaders);
        responseEntity = restTemplate.exchange(
                baseURL.concat("/sale/").concat(bidVO.getSale().getId().toString()).concat("/bid"),
                HttpMethod.POST, bidHttpEntity, AuctionResponse.class);
        response = responseEntity.getBody();
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatusCode());
        assertTrue(response.getErrorVOs().size() > 0);
        for (ErrorVO errorVO : response.getErrorVOs()) {
            assertEquals("Sale not found.", errorVO.getMessage());
        }
    }

    /**
     * Test Bid create.
     * Expected: Failed 400 BadRequest.
     */
    @Test
    public void testBidWhenBidPriceInvalid() throws NoSuchAlgorithmException {
        String email = "treddy4_bid3@sapient.com";
        //ceate user .
        AuctionResponse response = registerUser(email);
        assertEquals(response.getStatusCode(), Response.Status.CREATED.getStatusCode());
        assertEquals(response.getMessage(), "User registration successful.");

        //setting auth header.
        setAuthHeader(email, "password1");
        //create sale.
        SaleVO saleVO = sale(Optional.of(email));
        HttpEntity<SaleVO> httpEntity = new HttpEntity<>(saleVO, httpHeaders);
        ResponseEntity<AuctionResponse> responseEntity = restTemplate.exchange(baseURL.concat("/sale"), HttpMethod.POST, httpEntity, AuctionResponse.class);
        response = responseEntity.getBody();
        assertEquals(response.getStatusCode(), Response.Status.CREATED.getStatusCode());
        assertNotNull(responseEntity.getHeaders().getLocation());
        //keep the saleId
        Long saleId = response.getSaleVOs().stream().findFirst().get().getId();

        //post bid 1.
        BidVO bidVO = bid(email, saleId, 1000);
        HttpEntity<BidVO> bidHttpEntity = new HttpEntity<>(bidVO, httpHeaders);
        responseEntity = restTemplate.exchange(
                baseURL.concat("/sale/").concat(saleId.toString()).concat("/bid"),
                HttpMethod.POST, bidHttpEntity, AuctionResponse.class);
        response = responseEntity.getBody();
        assertEquals(response.getStatusCode(), Response.Status.CREATED.getStatusCode());
        assertNotNull(responseEntity.getHeaders().getLocation());
        assertEquals(response.getMessage(), "Bid posted successfully.");
        assertNotNull(response.getBidVO());

        //post bid 1.
        bidVO = bid(email, saleId, 900);
        bidHttpEntity = new HttpEntity<>(bidVO, httpHeaders);
        responseEntity = restTemplate.exchange(
                baseURL.concat("/sale/").concat(saleId.toString()).concat("/bid"),
                HttpMethod.POST, bidHttpEntity, AuctionResponse.class);
        response = responseEntity.getBody();
        assertEquals(response.getStatusCode(), Response.Status.BAD_REQUEST.getStatusCode());
        assertTrue(response.getErrorVOs().size() > 0);
        for (ErrorVO errorVO : response.getErrorVOs()) {
            assertEquals("Bid price is Invalid.", errorVO.getMessage());
        }
    }

    /**
     * Test Bid create.
     * Expected: Failed 401 UnAuthorized.
     */
    @Test
    public void testBidUnAuthorized() throws NoSuchAlgorithmException {
        String email = "treddy4_bid4@sapient.com";
        //ceate user .
        AuctionResponse response = registerUser(email);
        assertEquals(response.getStatusCode(), Response.Status.CREATED.getStatusCode());
        assertEquals(response.getMessage(), "User registration successful.");

        //setting auth header.
        setAuthHeader(email, "password1");
        //create sale.
        SaleVO saleVO = sale(Optional.of(email));
        HttpEntity<SaleVO> httpEntity = new HttpEntity<>(saleVO, httpHeaders);
        ResponseEntity<AuctionResponse> responseEntity = restTemplate.exchange(baseURL.concat("/sale"), HttpMethod.POST, httpEntity, AuctionResponse.class);
        response = responseEntity.getBody();
        assertEquals(response.getStatusCode(), Response.Status.CREATED.getStatusCode());
        assertNotNull(responseEntity.getHeaders().getLocation());

        //setting auth header.
        setAuthHeader(email, "invalidpassword");
        //post bid.
        BidVO bidVO = bid(email, response.getSaleVOs().stream().findFirst().get().getId(), 1000);
        HttpEntity<BidVO> bidHttpEntity = new HttpEntity<>(bidVO, httpHeaders);
        responseEntity = restTemplate.exchange(
                baseURL.concat("/sale/").concat(bidVO.getSale().getId().toString()).concat("/bid"),
                HttpMethod.POST, bidHttpEntity, AuctionResponse.class);
        response = responseEntity.getBody();
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatusCode());
        assertTrue(response.getErrorVOs().size() > 0);
        for (ErrorVO errorVO : response.getErrorVOs()) {
            assertEquals(errorVO.getMessage(), "Authentication failed.");
        }
    }

    /**
     * Test get Latest Bid.
     * Expected: Success 200 OK.
     */
    @Test
    public void testGetLatestBid() throws NoSuchAlgorithmException {
        String email = "treddy4_bid5@sapient.com";
        //ceate user .
        AuctionResponse response = registerUser(email);
        assertEquals(response.getStatusCode(), Response.Status.CREATED.getStatusCode());
        assertEquals(response.getMessage(), "User registration successful.");

        //setting auth header.
        setAuthHeader(email, "password1");
        //create sale.
        SaleVO saleVO = sale(Optional.of(email));
        HttpEntity<SaleVO> httpEntity = new HttpEntity<>(saleVO, httpHeaders);
        ResponseEntity<AuctionResponse> responseEntity = restTemplate.exchange(baseURL.concat("/sale"), HttpMethod.POST, httpEntity, AuctionResponse.class);
        response = responseEntity.getBody();
        assertEquals(response.getStatusCode(), Response.Status.CREATED.getStatusCode());
        assertNotNull(responseEntity.getHeaders().getLocation());
        Long saleId = response.getSaleVOs().stream().findFirst().get().getId();
        //setting auth header.
        setAuthHeader(email, "password1");

        //post bid 1.
        BidVO bidVO = bid(email, saleId, 1200);
        HttpEntity<BidVO> bidHttpEntity = new HttpEntity<>(bidVO, httpHeaders);
        responseEntity = restTemplate.exchange(
                baseURL.concat("/sale/").concat(bidVO.getSale().getId().toString()).concat("/bid"),
                HttpMethod.POST, bidHttpEntity, AuctionResponse.class);
        response = responseEntity.getBody();
        assertEquals(response.getStatusCode(), Response.Status.CREATED.getStatusCode());
        assertNotNull(responseEntity.getHeaders().getLocation());
        assertEquals(response.getMessage(), "Bid posted successfully.");
        assertNotNull(response.getBidVO());

        //creating user 2.
        String email2 = "treddy4_bid6@sapient.com";
        //ceate user .
        response = registerUser(email2);
        assertEquals(response.getStatusCode(), Response.Status.CREATED.getStatusCode());
        assertEquals(response.getMessage(), "User registration successful.");

        //set auth header.
        setAuthHeader(email2, "password1");

        //post bid 2.
        bidVO = bid(email2, saleId, 1300);
        bidHttpEntity = new HttpEntity<>(bidVO, httpHeaders);
        responseEntity = restTemplate.exchange(
                baseURL.concat("/sale/").concat(bidVO.getSale().getId().toString()).concat("/bid"),
                HttpMethod.POST, bidHttpEntity, AuctionResponse.class);
        response = responseEntity.getBody();
        assertEquals(response.getStatusCode(), Response.Status.CREATED.getStatusCode());
        assertNotNull(responseEntity.getHeaders().getLocation());
        assertEquals(response.getMessage(), "Bid posted successfully.");
        assertNotNull(response.getBidVO());

        //get latest bid for the sale 1.
        HttpEntity getBidEntitiy = new HttpEntity(httpHeaders);
        responseEntity = restTemplate.exchange(
                baseURL.concat("/sale/").concat("" + saleId).concat("/bid"),
                HttpMethod.GET, getBidEntitiy, AuctionResponse.class);
        response = responseEntity.getBody();
        assertNotNull(response);
        assertNotNull(response.getBidVO());
        assertNotNull(response.getBidVO().getId());
        assertEquals(saleId, response.getBidVO().getSale().getId());
        assertEquals(email2, response.getBidVO().getUser().getEmail());
        assertEquals(1300, response.getBidVO().getPrice().intValue());
    }

    @After
    public void tearDown() {
        super.tearDown();
    }

}
