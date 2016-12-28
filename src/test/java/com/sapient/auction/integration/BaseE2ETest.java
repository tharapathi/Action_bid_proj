package com.sapient.auction.integration;

import com.sapient.auction.SapAuctionSiteApplication;
import com.sapient.auction.common.model.AuctionResponse;
import com.sapient.auction.common.model.BidVO;
import com.sapient.auction.common.model.SaleVO;
import com.sapient.auction.common.model.UserVO;
import com.sapient.auction.user.util.PasswordUtil;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.MediaType;
import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

/**
 * Created by Lovababu on 11/20/2016.
 */
@SpringApplicationConfiguration(classes = SapAuctionSiteApplication.class)
@WebIntegrationTest(randomPort = true)
public class BaseE2ETest {

    protected RestTemplate restTemplate = null;

    @Value("${local.server.port}")  //boot injects the port.
    private int port;

    String baseURL = null;


    HttpHeaders httpHeaders;

    @Before
    public void setUp() {
        restTemplate = new TestRestTemplate();
        baseURL = "http://localhost:" + port + "/auction";
        httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", MediaType.APPLICATION_JSON);
    }

    void setAuthHeader(String email, String password) throws NoSuchAlgorithmException {
        String basicAuth = email + ":" + PasswordUtil.hash(password);
        if (httpHeaders.containsKey("Authorization")) {
            httpHeaders.remove("Authorization");
        }
        httpHeaders.add("Authorization", "Basic " + Base64.getEncoder().encodeToString(basicAuth.getBytes()));
    }

    protected UserVO user(String email) {


        UserVO userVO = UserVO.builder()
                .withFirstName("tharapathi")
                .withLastName("reddy")
                .withEmail(email)
                .withPassword("password1")
                .withContact("7405207197")
                .withAddress("Bangalore").build();

        return userVO;
    }

    protected SaleVO sale(Optional<String> email) {

        SaleVO saleVO = SaleVO.builder()
                .withEndTime(new Date())
                .withPrice(new BigDecimal(15000))
                .withProductId("124512-Len")
                .withProductDesc("Lenovo thinkpad.")
                .withProductName("Lenovo Laptop")
                .withProductImageUrl("http://weknowyourdreams.com/images/house/house-05.jpg")
                .withUserVO(UserVO.builder().withEmail(email.isPresent() ? email.get(): "dpadala@sapient.com").build())
                .withProductType("Electronic").build();
        return saleVO;
    }

    protected BidVO bid(String email, Long saleId, int price) {
        BidVO bidVO = BidVO.builder()
                .withPrice(new BigDecimal(price))
                .withUser(UserVO.builder()
                        .withEmail(email).build())
                .withSale(SaleVO.builder()
                        .withId(saleId).build())
                .build();

        return bidVO;
    }

    protected AuctionResponse registerUser(String email) {

        HttpEntity httpEntity = new HttpEntity(user(email), httpHeaders);
        ResponseEntity responseEntity = restTemplate.exchange(baseURL.concat("/user"), HttpMethod.POST, httpEntity, AuctionResponse.class);
        return  (AuctionResponse) responseEntity.getBody();
    }


    @After
    public void tearDown() {
        restTemplate = null;
        Path path = Paths.get(System.getProperty("user.dir"), "db-data");
        File file = path.toFile();
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                f.deleteOnExit();
            }
        }
    }

}
